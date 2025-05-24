package JustGrades.app.repository;

import JustGrades.app.model.Course;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @EntityGraph(attributePaths = {"completionRequirements", "enrollRequirements", "specializations"})
    List<Course> findAllByOrderByNameAsc();

    @Modifying
    @Transactional
    @Query(value = "CALL open_registration(:id)", nativeQuery = true)
    void openRegistration(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "CALL close_registration(:id)", nativeQuery = true)
    void closeRegistration(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "CALL close_course(:id)", nativeQuery = true)
    void closeCourse(@Param("id") Long id);

    @Query("SELECT c.id FROM Course c WHERE c.status NOT IN ('closed', 'closed registration')")
    List<Long> findCoursesToClose();
}
