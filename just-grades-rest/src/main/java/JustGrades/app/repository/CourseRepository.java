package JustGrades.app.repository;

import JustGrades.app.model.Course;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long>{

    List<Course> findAll(Sort sort);
}
