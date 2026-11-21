package co.edu.unicauca.saberpro.validation;

import java.util.List;

public record ValidationRequest(String statement,
                                List<OptionDto> options,
                                String correctKey,
                                String justification) {
    public record OptionDto(String key, String text) {}
}
