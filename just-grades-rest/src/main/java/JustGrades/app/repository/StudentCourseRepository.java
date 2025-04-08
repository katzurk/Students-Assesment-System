package JustGrades.app.repository;

import JustGrades.app.model.Course;
import JustGrades.app.model.StudentCourse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends CrudRepository<StudentCourse, Long>{
    List<Course> findByStudentEmail(String email);

    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.userId = :id")
    List<Course> findByStudentUserId(Long id);
}
