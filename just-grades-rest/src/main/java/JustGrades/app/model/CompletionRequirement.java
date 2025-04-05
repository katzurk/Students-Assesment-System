package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name = "completion_req")
public class CompletionRequirement {
    @Id
    @Column(name = "req_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requirementsSeq")
    @SequenceGenerator(name = "requirementsSeq", sequenceName = "REQUIREMENTS_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "min_score")
    @NotNull(message = "min_score is mandatory")
    private int minScore;

    @Column(name = "type")
    @NotBlank(message = "type is mandatory")
    private String type;

    public CompletionRequirement() {
    }

    public CompletionRequirement(int minScore, String type) {
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
