package co.edu.unicauca.saberpro.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubmitQuestionListener {

    private static final Logger log = LoggerFactory.getLogger(SubmitQuestionListener.class);
    private final LifecycleService service;

    public SubmitQuestionListener(LifecycleService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onSubmitQuestion(SubmitQuestionForReview command) {
        log.info("Comando recibido: {}", command);
        service.submitForReview(command);
    }
}
