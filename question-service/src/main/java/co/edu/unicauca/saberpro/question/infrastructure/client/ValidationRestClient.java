package co.edu.unicauca.saberpro.question.infrastructure.client;

import co.edu.unicauca.saberpro.question.application.exception.ValidationServiceUnavailableException;
import co.edu.unicauca.saberpro.question.application.port.QuestionValidationPort;
import co.edu.unicauca.saberpro.question.application.port.ValidationResult;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

/** Invocación síncrona HTTP REST: POST {validation.service.url}/validate */
@Component
public class ValidationRestClient implements QuestionValidationPort {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ValidationRestClient(RestTemplateBuilder builder,
                                @Value("${validation.service.url}") String baseUrl) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.baseUrl = baseUrl;
    }

    @Override
    public ValidationResult validate(Question question) {
        ValidationRequestDto body = new ValidationRequestDto(
                question.getStatement(),
                question.getOptions().stream()
                        .map(o -> new ValidationRequestDto.OptionDto(o.getKey(), o.getText()))
                        .toList(),
                question.getCorrectKey(),
                question.getJustification());
        try {
            restTemplate.postForEntity(baseUrl + "/validate", body, ValidationResponseDto.class);
            return new ValidationResult(true, List.of());                 // 200 OK
        } catch (HttpClientErrorException.BadRequest ex) {                // 400 Bad Request
            ValidationResponseDto dto = ex.getResponseBodyAs(ValidationResponseDto.class);
            List<String> errors = (dto != null && dto.errors() != null)
                    ? dto.errors() : List.of("La validación estructural falló");
            return new ValidationResult(false, errors);
        } catch (ResourceAccessException ex) {                            // servicio caído
            throw new ValidationServiceUnavailableException(
                    "Validation Service no disponible en " + baseUrl, ex);
        }
    }
}
