package JustGrades.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "student_courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_course_seq")
    @SequenceGenerator(name = "student_course_seq", sequenceName = "STUDENT_COURSES_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Temporal(TemporalType.DATE)
    @Column(name = "enrolled_on")
    private Date enrolledOn = new Date();
}

