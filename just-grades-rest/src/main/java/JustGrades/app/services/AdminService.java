package JustGrades.app.services;

import JustGrades.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void openSemester() {
        userRepository.openSemester();
        System.out.println("oooooo");
    }

    public void closeSemester() {
        userRepository.closeSemester();
    }
}
