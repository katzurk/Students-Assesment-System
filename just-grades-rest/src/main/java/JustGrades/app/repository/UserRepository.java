package JustGrades.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import JustGrades.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    User findUserByUserId(long l);

    @Modifying
    @Transactional
    @Query(value = "CALL open_semester()", nativeQuery = true)
    void openSemester();

    @Modifying
    @Transactional
    @Query(value = "CALL close_semester()", nativeQuery = true)
    void closeSemester();
}
