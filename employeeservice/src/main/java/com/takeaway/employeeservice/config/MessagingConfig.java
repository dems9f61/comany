package com.takeaway.employeeservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 13:38
 * <p/>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "exchange")
@Validated
public class MessagingConfig
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotBlank
    private String exchangeName;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         RetryTemplate retryTemplate,
                                         Jackson2JsonMessageConverter messageConverter,
                                         MessagePostProcessor messagePostProcessor)
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(retryTemplate);
        template.setExchange(exchangeName);
        template.setMessageConverter(messageConverter);
        template.setBeforePublishPostProcessors(messagePostProcessor);
        return template;
    }

    @Bean
    public MessagePostProcessor messagePostProcessor()
    {
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader("timestamp", ZonedDateTime.now());
            return message;
        };
    }

    @Bean
    public RetryTemplate retryTemplate()
    {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ClassMapper classMapper, ObjectMapper objectMapper)
    {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(objectMapper);
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    public ClassMapper classMapper()
    {
        return new DefaultJackson2JavaTypeMapper();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
