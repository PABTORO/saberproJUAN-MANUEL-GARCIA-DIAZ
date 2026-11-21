package co.edu.unicauca.saberpro.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Autoridad centralizada del estado de las preguntas. */
@Service
public class LifecycleService {

    private static final Logger log = LoggerFactory.getLogger(LifecycleService.class);
    private final Map<Long, QuestionState> states = new ConcurrentHashMap<>();

    public void submitForReview(SubmitQuestionForReview cmd) {
        QuestionState current = states.getOrDefault(cmd.questionId(), QuestionState.DRAFT);
        if (current == QuestionState.PENDING_REVIEW) {
            log.warn("Pregunta {} ya estaba en PENDING_REVIEW (operación {}). Se ignora (idempotente).",
                    cmd.questionId(), cmd.operationId());
            return;
        }
        states.put(cmd.questionId(), QuestionState.PENDING_REVIEW);
        log.info("Pregunta {}: {} -> PENDING_REVIEW (operación {})",
                cmd.questionId(), current, cmd.operationId());
    }

    public QuestionState getState(Long questionId) {
        return states.getOrDefault(questionId, QuestionState.DRAFT);
    }
}
