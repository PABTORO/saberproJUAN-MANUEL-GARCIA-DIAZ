package co.edu.unicauca.saberpro.question.application.port;

import co.edu.unicauca.saberpro.question.domain.model.Question;

public interface QuestionValidationPort {
    ValidationResult validate(Question question);
}
