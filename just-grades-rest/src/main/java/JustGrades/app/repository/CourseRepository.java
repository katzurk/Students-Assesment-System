package JustGrades.app.repository;

import JustGrades.app.model.Course;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long>{

    @EntityGraph(attributePaths = {"completionRequirements", "enrollRequirements"})
    List<Course> findAllByOrderByNameAsc();
}
