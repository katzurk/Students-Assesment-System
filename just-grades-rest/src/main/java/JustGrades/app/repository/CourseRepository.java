package JustGrades.app.repository;

import JustGrades.app.model.Course;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @EntityGraph(attributePaths = {"completionRequirements", "enrollRequirements", "specializations"})
    List<Course> findAllByOrderByNameAsc();
}
