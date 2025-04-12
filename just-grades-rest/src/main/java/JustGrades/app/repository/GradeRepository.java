package JustGrades.app.repository;

import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long>{
    @Query("SELECT g.course FROM Grade g JOIN g.course c WHERE g.student.userId = :id")
    List<Course> findByStudentId(Long id);

    List<Grade> findByStudentUserIdAndCourseId(Long id, Long courseId);
}
