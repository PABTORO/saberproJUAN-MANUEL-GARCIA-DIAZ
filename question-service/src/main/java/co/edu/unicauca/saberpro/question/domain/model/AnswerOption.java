package co.edu.unicauca.saberpro.question.domain.model;

/** Opción de respuesta. Clase Java pura (sin anotaciones relacionales). */
public class AnswerOption {
    private Long id;
    private String key;
    private String text;

    public AnswerOption(Long id, String key, String text) {
        this.id = id;
        this.key = key;
        this.text = text;
    }

    public Long getId() { return id; }
    public String getKey() { return key; }
    public String getText() { return text; }
}
