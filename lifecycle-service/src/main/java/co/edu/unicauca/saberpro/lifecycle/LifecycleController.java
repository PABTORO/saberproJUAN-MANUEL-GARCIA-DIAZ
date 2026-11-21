package co.edu.unicauca.saberpro.lifecycle;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Endpoint de apoyo para verificar el estado desde Postman. */
@RestController
@RequestMapping("/lifecycle")
public class LifecycleController {

    private final LifecycleService service;

    public LifecycleController(LifecycleService service) {
        this.service = service;
    }

    @GetMapping("/questions/{id}")
    public Map<String, Object> state(@PathVariable Long id) {
        return Map.of("questionId", id, "state", service.getState(id).name());
    }
}
