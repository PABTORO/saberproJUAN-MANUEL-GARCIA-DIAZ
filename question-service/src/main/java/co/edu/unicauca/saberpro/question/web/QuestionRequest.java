package co.edu.unicauca.saberpro.question.web;

import java.util.List;

public record QuestionRequest(String statement, List<OptionDto> options,
                              String correctKey, String justification) {
    public record OptionDto(String key, String text) {}
}
