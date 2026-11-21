package co.edu.unicauca.saberpro.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "saberpro.direct.exchange";
    public static final String QUEUE = "question.submit.review.queue";
    public static final String ROUTING_KEY = "question.submit";

    @Bean
    DirectExchange saberproExchange() { return new DirectExchange(EXCHANGE, true, false); }

    @Bean
    Queue submitReviewQueue() { return QueueBuilder.durable(QUEUE).build(); }

    @Bean
    Binding submitBinding(Queue submitReviewQueue, DirectExchange saberproExchange) {
        return BindingBuilder.bind(submitReviewQueue).to(saberproExchange).with(ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
