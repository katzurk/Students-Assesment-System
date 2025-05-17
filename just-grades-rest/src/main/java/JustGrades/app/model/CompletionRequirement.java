package JustGrades.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "completion_requirements")
@Setter
@Getter
public class CompletionRequirement {
    @Id
    @Column(name = "completion_req_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requirementsSeq")
    @SequenceGenerator(name = "requirementsSeq", sequenceName = "REQUIREMENTS_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "min_score")
    @NotNull(message = "min_score is mandatory")
    private int minScore;

    @Column(name = "type")
    @NotBlank(message = "type is mandatory")
    private String type;

    @Column(name = "course_id")
    private Long courseId;

    public CompletionRequirement() {
    }

    public CompletionRequirement(int minScore, String type, Long courseId) {
        this.minScore = minScore;
        this.type = type;
        this.courseId = courseId;
    }
}
