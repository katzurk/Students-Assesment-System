package JustGrades.app.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import JustGrades.app.model.Course;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findAll_returnCourses() {
        List<Course> courses = courseRepository.findAllByOrderByNameAsc();

        assertThat(courses).hasSize(130);
        assertThat(courses).anyMatch(c -> !c.getSpecializations().isEmpty());
    }
}
