package co.edu.unicauca.saberpro.question.application.port;

import co.edu.unicauca.saberpro.question.application.command.SubmitQuestionForReview;

public interface QuestionCommandPort {
    void publish(SubmitQuestionForReview command);
}
