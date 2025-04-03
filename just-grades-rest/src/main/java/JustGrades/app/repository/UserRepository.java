package JustGrades.app.repository;

import java.util.Optional;

import JustGrades.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    User findByEmail(String email);
}
