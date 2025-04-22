package JustGrades.app.repository;

import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long>{
    List<Grade> findByStudentEmailAndCourseId(String email, Long courseId);

    List<Grade> findByCourseIdOrderByStudentLastNameAsc(Long courseId);

}
