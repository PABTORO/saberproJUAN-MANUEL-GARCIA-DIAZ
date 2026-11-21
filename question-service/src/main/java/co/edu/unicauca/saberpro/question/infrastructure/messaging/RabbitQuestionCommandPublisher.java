package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.application.command.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.port.QuestionCommandPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitQuestionCommandPublisher implements QuestionCommandPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitQuestionCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(SubmitQuestionForReview command) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, command);
    }
}
