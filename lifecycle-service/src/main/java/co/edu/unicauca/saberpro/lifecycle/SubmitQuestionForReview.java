package co.edu.unicauca.saberpro.lifecycle;

/** Mismo contrato JSON que publica el Question Service. */
public record SubmitQuestionForReview(String operationId, Long questionId, String requestedAt) {}
