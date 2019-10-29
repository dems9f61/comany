package com.takeaway.eventservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * User: StMinko Date: 20.03.2019 Time: 13:43
 *
 * <p>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "amqp")
@Validated
@RequiredArgsConstructor
public class MessagingConfig
{
  // =========================== Class Variables ===========================

  private static final String ERROR = "error";

  private static final String DEAD_LETTER = "deadLetter";

  // =============================  Variables  =============================

  @NotBlank
  private String exchangeName;

  @NotBlank
  private String queueName;

  @NotBlank
  private String routingKey;

  @Min(value = 1)
  private int concurrentConsumers;

  @Max(value = 20)
  private int maxConcurrentConsumers;

  private final ConnectionFactory cachingConnectionFactory;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter(ClassMapper classMapper, ObjectMapper objectMapper)
  {
    Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(objectMapper);
    jsonConverter.setClassMapper(classMapper);
    jsonConverter.setCreateMessageIds(true);
    return jsonConverter;
  }

  @Bean
  public ClassMapper classMapper()
  {
    return new DefaultJackson2JavaTypeMapper();
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
        Jackson2JsonMessageConverter converter,
        MethodInterceptor retryOperationsInterceptor)
  {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(converter);
    factory.setConcurrentConsumers(concurrentConsumers);
    factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
    factory.setAdviceChain(retryOperationsInterceptor);
    return factory;
  }

  @Bean
  public RabbitAdmin admin()
  {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
    rabbitAdmin.afterPropertiesSet();
    return rabbitAdmin;
  }

  @Primary
  @Bean
  public MethodInterceptor interceptor(RabbitTemplate rabbitTemplate)
  {
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(1000);

    return RetryInterceptorBuilder.stateless()
          .backOffPolicy(backOffPolicy)
          .maxAttempts(3)
          .recoverer(new RepublishMessageRecoverer(rabbitTemplate, errorQueue().getName(), routingKey))
          .build();
  }

  @Bean
  public Exchange exchange()
  {
    TopicExchange deadLetterExchange = new TopicExchange(exchangeName, true, false);
    deadLetterExchange.setAdminsThatShouldDeclare(admin());
    return deadLetterExchange;
  }

  @Bean
  public Exchange deadLetterExchange()
  {
    DirectExchange deadLetterExchange = new DirectExchange(exchangeName + "." + DEAD_LETTER, true, false);
    deadLetterExchange.setAdminsThatShouldDeclare(admin());
    return deadLetterExchange;
  }

  @Bean
  public Exchange errorExchange()
  {
    DirectExchange errorExchange = new DirectExchange(exchangeName + "." + ERROR, true, false);
    errorExchange.setAdminsThatShouldDeclare(admin());
    return errorExchange;
  }

  @Bean
  public Queue queue()
  {
    Queue queue = QueueBuilder.durable(queueName)
              .withArgument("x-message-ttl", 10000)
              .withArgument("x-dead-letter-exchange", exchangeName + "." + DEAD_LETTER)
              .withArgument("x-dead-letter-routing-key", routingKey)
              .build();
    queue.setAdminsThatShouldDeclare(admin());
    return queue;
  }

  @Bean
  public Queue deadLetterQueue()
  {
    Queue queue = QueueBuilder.durable(queueName + "." + DEAD_LETTER).build();
    queue.setAdminsThatShouldDeclare(admin());
    return queue;
  }

  @Bean
  public Queue errorQueue()
  {
    Queue queue = QueueBuilder.durable(queueName + "." + ERROR).build();
    queue.setAdminsThatShouldDeclare(admin());
    return queue;
  }

  @Bean
  public Binding queueBinding()
  {
    return BindingBuilder.bind(queue()).to(exchange()).with(routingKey).noargs();
  }

  @Bean
  public Binding queueBindingDlx()
  {
    return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(routingKey).noargs();
  }

  @Bean
  public Binding queueBindingError()
  {
    return BindingBuilder.bind(errorQueue()).to(errorExchange()).with(routingKey).noargs();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
