package JustGrades.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AdminService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdminService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void openSemester() {
        jdbcTemplate.execute("CALL open_semester()");
    }

    public void closeSemester() {
        jdbcTemplate.execute("CALL close_semester()");
    }
}
