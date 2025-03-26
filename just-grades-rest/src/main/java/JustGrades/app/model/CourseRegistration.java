package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "course_reg")
public class CourseRegistration {
    @Id
    @Column(name = "reg_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registationSeq")
    @SequenceGenerator(name = "registationSeq", sequenceName = "REGISTRATION_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "regulation_text")
    @NotBlank(message = "regulation_text is mandatory")
    private String regulationText;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    // @NotNull(message = "course is mandatory")
    private Course course;

    public CourseRegistration() {
    }

    public CourseRegistration(String regulationText, Course course) {
        this.regulationText = regulationText;
        this.course = course;
    }

    public long getId() {
        return this.id;
    }

    public String getRegulationText() {
        return this.regulationText;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setRegulationText(String newRegulationText) {
        this.regulationText = newRegulationText;
    }

    public void setCourse(Course newCourse) {
        this.course = newCourse;
    }
}