package co.edu.unicauca.saberpro.question.infrastructure.client;

import java.util.List;

public record ValidationResponseDto(boolean valid, List<String> errors) {}
