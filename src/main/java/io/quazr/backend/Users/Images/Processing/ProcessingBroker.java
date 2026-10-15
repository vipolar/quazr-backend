package io.quazr.backend.Users.Images.Processing;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;

@Configuration
public class ProcessingBroker {
    @Value("${rabbitmq.images.exchange}")
    private String imagesExchange;

    public String getImagesExchange() {
        return this.imagesExchange;
    }

    @Value("${rabbitmq.images.tasks.queue}")
    private String imagesTasksQueue;

    public String getImagesTasksQueue() {
        return this.imagesTasksQueue;
    }

    @Value("${rabbitmq.images.tasks.routing-key}")
    private String imagesTasksRoutingKey;

    public String getImagesTasksRoutingKey() {
        return this.imagesTasksRoutingKey;
    }

    @Value("${rabbitmq.images.tasks.successes.queue}")
    private String imagesTasksSuccessesQueue;

    public String getImagesTasksSuccessesQueue() {
        return this.imagesTasksSuccessesQueue;
    }

    @Value("${rabbitmq.images.tasks.successes.routing-key}")
    private String imagesTasksSuccessesRoutingKey;

    public String getImagesTasksSuccessesRoutingKey() {
        return this.imagesTasksSuccessesRoutingKey;
    }

    @Value("${rabbitmq.images.tasks.failures.queue}")
    private String imagesTasksFailuresQueue;

    public String getImagesTasksFailuresQueue() {
        return this.imagesTasksFailuresQueue;
    }

    @Value("${rabbitmq.images.tasks.failures.routing-key}")
    private String imagesTasksFailuresRoutingKey;

    public String getImagesTasksFailuresRoutingKey() {
        return this.imagesTasksFailuresRoutingKey;
    }

    @Value("${rabbitmq.images.dlq.routing-key}")
    private String imagesDLQRoutingKey;

    public String getImagesDLQRoutingKey() {
        return this.imagesDLQRoutingKey;
    }

    @Value("${rabbitmq.images.dlq.exchange}")
    private String imagesDLQExchange;

    public String getImagesDLQExchange() {
        return this.imagesDLQExchange;
    }

    @Value("${rabbitmq.images.dlq.queue}")
    private String imagesDLQQueue;

    public String getImagesDLQQueue() {
        return this.imagesDLQQueue;
    }

    @Bean
    public DirectExchange imagesExchange() {
        return new DirectExchange(imagesExchange);
    }

    @Bean 
    public Queue imagesTasksQueue() {
        return QueueBuilder.durable(imagesTasksQueue)
            .deadLetterRoutingKey(imagesDLQRoutingKey)
            .deadLetterExchange(imagesDLQExchange)
            .build();
    }

    @Bean
    public Binding imagesTasksBinding() {
        return BindingBuilder.bind(imagesTasksQueue()).to(imagesExchange())
                .with(imagesTasksRoutingKey);
    }

    @Bean
    public Queue imagesTasksSuccessesQueue() {
        return QueueBuilder.durable(imagesTasksSuccessesQueue)
            .deadLetterRoutingKey(imagesDLQRoutingKey)
            .deadLetterExchange(imagesDLQExchange)
            .build();
    }

    @Bean
    public Binding imagesTasksSuccessesBinding() {
        return BindingBuilder.bind(imagesTasksSuccessesQueue()).to(imagesExchange())
                .with(imagesTasksSuccessesRoutingKey);
    }

    @Bean
    public Queue imagesTasksFailuresQueue() {
        return QueueBuilder.durable(imagesTasksFailuresQueue)
            .deadLetterRoutingKey(imagesDLQRoutingKey)
            .deadLetterExchange(imagesDLQExchange)
            .build();
    }

    @Bean
    public Binding imagesTasksFailureBinding() {
        return BindingBuilder.bind(imagesTasksFailuresQueue()).to(imagesExchange())
                .with(imagesTasksFailuresRoutingKey);
    }

    @Bean
    public DirectExchange imagesDLQExchange() {
        return new DirectExchange(imagesDLQExchange);
    }

    @Bean
    public Queue imagesDLQQueue() {
        return QueueBuilder.durable(imagesDLQQueue).build();
    }

    @Bean
    public Binding imagesDLQBinding() {
        return BindingBuilder.bind(imagesDLQQueue()).to(imagesDLQExchange())
                .with(imagesDLQRoutingKey);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory imagesBrokerPrefetchContainerFactory(ConnectionFactory connectionFactory, JacksonJsonMessageConverter messageConverter, RabbitTemplate rabbitTemplate) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setPrefetchCount(1);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(4);

        factory.setErrorHandler(t -> {
            if (t instanceof ListenerExecutionFailedException) {
                ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
                Message failed = lefe.getFailedMessage();
                try {
                    if (failed != null) {
                        rabbitTemplate.send(this.imagesDLQExchange, this.imagesDLQRoutingKey, failed);
                    }
                } catch (AmqpException ex) {
                    System.err.println("Failed to publish to DLQ: " + ex.getMessage());
                }
            }

            throw new AmqpRejectAndDontRequeueException("Listener failed, sent to DLQ", t);
        });
        
        return factory;
    }
}
