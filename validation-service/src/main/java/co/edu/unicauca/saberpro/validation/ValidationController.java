package co.edu.unicauca.saberpro.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationController {

    private final QuestionValidator validator;

    public ValidationController(QuestionValidator validator) {
        this.validator = validator;
    }

    /** 200 OK si es válida; 400 Bad Request con la lista de errores si no. */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validate(@RequestBody ValidationRequest request) {
        ValidationResponse result = validator.validate(request);
        return result.valid() ? ResponseEntity.ok(result)
                              : ResponseEntity.badRequest().body(result);
    }
}
