package JustGrades.app.repository;

import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Student;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    List<CourseRegistration> findByStudent(Student student);

    @Query(value = "SELECT * FROM course_registrations WHERE student_id = :studentId AND status LIKE :status", nativeQuery = true)
    List<CourseRegistration> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") String status);

    @Query("SELECT r.course FROM CourseRegistration r WHERE r.student.email = :email")
    List<Course> findCoursesByStudentEmail(@Param("email") String email);

    CourseRegistration findByStudentEmailAndCourseId(String email, Long courseId);

    @Query("SELECT new JustGrades.app.dto.CourseRegisteredDTO(c, (cr IS NOT NULL)) " +
            "FROM Course c " +
            "LEFT JOIN CourseRegistration cr ON cr.course.id = c.id AND cr.student.email = :email " +
            "WHERE c.status = 'opened'")
    List<CourseRegisteredDTO> findCoursesRegisteredByStudentEmail(String email);
}
