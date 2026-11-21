package co.edu.unicauca.saberpro.question.infrastructure.client;

import java.util.List;

public record ValidationRequestDto(String statement, List<OptionDto> options,
                                   String correctKey, String justification) {
    public record OptionDto(String key, String text) {}
}
