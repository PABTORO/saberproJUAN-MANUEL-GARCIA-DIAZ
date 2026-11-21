package co.edu.unicauca.saberpro.question.application.command;

/** Comando enviado por RabbitMQ hacia el Lifecycle Service. */
public record SubmitQuestionForReview(String operationId, Long questionId, String requestedAt) {}
