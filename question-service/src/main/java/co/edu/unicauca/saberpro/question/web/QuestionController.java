package co.edu.unicauca.saberpro.question.web;

import co.edu.unicauca.saberpro.question.application.service.QuestionApplicationService;
import co.edu.unicauca.saberpro.question.domain.model.AnswerOption;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionApplicationService service;

    public QuestionController(QuestionApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> create(@RequestBody QuestionRequest request) {
        List<AnswerOption> options = request.options() == null ? List.of()
                : request.options().stream().map(o -> new AnswerOption(null, o.key(), o.text())).toList();
        Question question = new Question(null, request.statement(), options,
                request.correctKey(), request.justification(), null);

        Question saved = service.create(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));   // 201
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmitResponse> submit(@PathVariable Long id) {
        String operationId = service.submitForReview(id);
        return ResponseEntity.accepted().body(new SubmitResponse(operationId, "PROCESSING")); // 202
    }

    private QuestionResponse toResponse(Question q) {
        return new QuestionResponse(q.getId(), q.getStatement(),
                q.getOptions().stream().map(o -> new QuestionResponse.OptionDto(o.getId(), o.getKey(), o.getText())).toList(),
                q.getCorrectKey(), q.getJustification(), q.getStatus().name());
    }
}
