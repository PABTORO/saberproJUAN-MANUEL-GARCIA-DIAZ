package co.edu.unicauca.saberpro.question.infrastructure.jpa.record;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_options")
public class AnswerOptionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_key", nullable = false, length = 10)
    private String key;

    @Column(name = "option_text", nullable = false, length = 1000)
    private String text;

    public AnswerOptionRecord() {}

    public AnswerOptionRecord(Long id, String key, String text) {
        this.id = id;
        this.key = key;
        this.text = text;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
