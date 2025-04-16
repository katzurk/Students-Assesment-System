package JustGrades.app.repository;

import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Student;
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

    @Query("SELECT r.course FROM CourseRegistration r WHERE r.student.userId = :studentId")
    List<Course> findCoursesByStudentId(long studentId);
}
