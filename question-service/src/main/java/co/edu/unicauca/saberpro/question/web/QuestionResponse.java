package co.edu.unicauca.saberpro.question.web;

import java.util.List;

public record QuestionResponse(Long id, String statement, List<OptionDto> options,
                               String correctKey, String justification, String status) {
    public record OptionDto(Long id, String key, String text) {}
}
