package JustGrades.app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import JustGrades.app.model.Course;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminRepository extends CrudRepository<Course, Long> {

    @Modifying
    @Transactional
    @Query(value = "CALL open_semester()", nativeQuery = true)
    void openSemester();

    @Modifying
    @Transactional
    @Query(value = "CALL close_semester()", nativeQuery = true)
    void closeSemester();

}

