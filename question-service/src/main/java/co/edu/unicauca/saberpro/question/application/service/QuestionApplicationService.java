package co.edu.unicauca.saberpro.question.application.service;

import co.edu.unicauca.saberpro.question.application.command.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.application.exception.QuestionValidationException;
import co.edu.unicauca.saberpro.question.application.port.QuestionCommandPort;
import co.edu.unicauca.saberpro.question.application.port.QuestionValidationPort;
import co.edu.unicauca.saberpro.question.application.port.ValidationResult;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class QuestionApplicationService {

    private final QuestionRepository repository;
    private final QuestionValidationPort validationPort;
    private final QuestionCommandPort commandPort;

    public QuestionApplicationService(QuestionRepository repository,
                                      QuestionValidationPort validationPort,
                                      QuestionCommandPort commandPort) {
        this.repository = repository;
        this.validationPort = validationPort;
        this.commandPort = commandPort;
    }

    /** Punto 1: valida por REST ANTES de guardar. Si falla -> 400, no se persiste nada. */
    public Question create(Question question) {
        ValidationResult result = validationPort.validate(question);
        if (!result.valid()) {
            throw new QuestionValidationException(result.errors());
        }
        question.markAsDraft();
        return repository.save(question);
    }

    /** Punto 3: publica el comando y retorna de inmediato el operationId. */
    public String submitForReview(Long questionId) {
        repository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));
        String operationId = UUID.randomUUID().toString();
        commandPort.publish(new SubmitQuestionForReview(operationId, questionId, Instant.now().toString()));
        return operationId;
    }
}
