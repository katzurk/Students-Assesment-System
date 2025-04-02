package JustGrades.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "course_reg")
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


    public Long getId() {
        return this.id;
    }

    public Course getComplitedCourse() {
        return this.complitedCourse;
    }

    public Integer getMinEcts() {
        return this.minEcts;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public void setComplitedCourseId(Course newcomplitedCourse) {
        this.complitedCourse = newcomplitedCourse;
    }

    public void setMinEcts(Integer newMinEcts) {
        this.minEcts = newMinEcts;
    }

    public void setCourseId(Long newCourse) {
        this.courseId = newCourse;
    }
}