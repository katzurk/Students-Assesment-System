package JustGrades.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_seq")
    @SequenceGenerator(name = "registration_seq", sequenceName = "REGISTRATION_SEQ", allocationSize = 1)
    @Column(name = "course_reg_id")
    private Long courseRegId;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
}