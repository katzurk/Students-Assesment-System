package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "completition_requierments")
public class CompletitionRequierment {
    @Id
    @Column(name = "req_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requiermentsSeq")
    @SequenceGenerator(name = "requiermentsSeq", sequenceName = "REQUIERMENTS_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "min_score")
    @NotBlank(message = "min_score is mandatory")
    private int minScore;

    @Column(name = "type")
    @NotBlank(message = "type is mandatory")
    private String type;

    public CompletitionRequierment() {
    }

    public CompletitionRequierment(int minScore, String type) {
        this.minScore = minScore;
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    public int getMinScore() {
        return this.minScore;
    }

    public String getType() {
        return this.type;
    }

    public void setMinScore(int newMinScore) {
        this.minScore = newMinScore;
    }

    public void setType(String newType) {
        this.type = newType;
    }

}
