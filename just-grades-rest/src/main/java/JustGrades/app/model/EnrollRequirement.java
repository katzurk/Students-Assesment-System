package JustGrades.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "enroll_requirements")
@Getter
@Setter
public class EnrollRequirement {
    @Id
    @Column(name = "reg_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registationSeq")
    @SequenceGenerator(name = "registationSeq", sequenceName = "REGISTRATION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "min_ects")
    private Integer minEcts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "complited_course_id", referencedColumnName = "course_id")
    private Course complitedCourse;

    @Column(name = "course_id")
    private Long courseId;

    public EnrollRequirement() {
    }

    public EnrollRequirement(Course complitedCourse, Long courseId) {
        this.complitedCourse = complitedCourse;
        this.courseId = courseId;
    }

    public EnrollRequirement(Integer minEcts, Long courseId) {
        this.minEcts = minEcts;
        this.courseId = courseId;
    }
}