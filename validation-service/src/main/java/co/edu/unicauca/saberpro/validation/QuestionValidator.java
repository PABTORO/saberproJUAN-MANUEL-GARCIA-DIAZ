package co.edu.unicauca.saberpro.validation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** Reglas de validación estructural (HU03). */
@Service
public class QuestionValidator {

    static final int REQUIRED_OPTIONS = 4;
    static final int MIN_JUSTIFICATION_LENGTH = 20;

    public ValidationResponse validate(ValidationRequest req) {
        List<String> errors = new ArrayList<>();

        if (isBlank(req.statement())) {
            errors.add("El enunciado es obligatorio.");
        }

        List<ValidationRequest.OptionDto> options = req.options();
        int count = options == null ? 0 : options.size();
        if (count != REQUIRED_OPTIONS) {
            errors.add("La pregunta debe tener exactamente " + REQUIRED_OPTIONS
                    + " opciones (recibidas: " + count + ").");
        }

        if (options != null) {
            Set<String> keys = options.stream()
                    .filter(Objects::nonNull)
                    .map(ValidationRequest.OptionDto::key)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (keys.size() != options.size()) {
                errors.add("Las opciones deben tener claves únicas y no vacías.");
            }
            if (isBlank(req.correctKey()) || !keys.contains(req.correctKey())) {
                errors.add("La clave correcta '" + req.correctKey() + "' no existe entre las opciones.");
            }
        } else {
            errors.add("La clave correcta no puede verificarse porque no hay opciones.");
        }

        if (isBlank(req.justification()) || req.justification().trim().length() < MIN_JUSTIFICATION_LENGTH) {
            errors.add("La justificación es obligatoria y debe tener al menos "
                    + MIN_JUSTIFICATION_LENGTH + " caracteres.");
        }

        return new ValidationResponse(errors.isEmpty(), errors);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
