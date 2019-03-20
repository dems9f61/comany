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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 13:43
 * <p/>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Validated
@RequiredArgsConstructor
public class MessagingConfig
{
    // =========================== Class Variables ===========================

    private static final String ERROR_QUEUE_NAME = "errorQueue";

    private static final String ERROR_EXCHANGE_NAME = "errorExchange";

    private static final String ERROR_ROUTING_KEY = "error";

    // =============================  Variables  =============================

    @NotBlank
    private String exchangeName;

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
        factory.setAdviceChain(retryOperationsInterceptor);
        return factory;
    }

    @Primary
    @Bean
    public MethodInterceptor interceptor(RabbitTemplate rabbitTemplate, @Qualifier(ERROR_QUEUE_NAME) Queue errorQueue)
    {

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);

        return RetryInterceptorBuilder.stateless()
                                      .backOffPolicy(backOffPolicy)
                                      .maxAttempts(3)
                                      .recoverer(new RepublishMessageRecoverer(rabbitTemplate, errorQueue.getName(), ERROR_ROUTING_KEY))
                                      .build();
    }

    @Bean
    public RabbitAdmin admin()
    {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        rabbitAdmin.afterPropertiesSet();
        return rabbitAdmin;
    }

    @Bean(name = ERROR_EXCHANGE_NAME)
    public Exchange errorExchange()
    {
        return new DirectExchange(ERROR_ROUTING_KEY);
    }

    @Bean(name = ERROR_QUEUE_NAME)
    public Queue errorQueue()
    {
        return new Queue(ERROR_ROUTING_KEY, true);
    }

    @Bean
    public Binding errorBinding(@Qualifier(ERROR_EXCHANGE_NAME) Exchange exchange, @Qualifier(ERROR_QUEUE_NAME) Queue queue)
    {
        return BindingBuilder.bind(queue)
                             .to(exchange)
                             .with(ERROR_ROUTING_KEY)
                             .noargs();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
