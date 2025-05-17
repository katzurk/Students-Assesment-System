package JustGrades.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Student;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.StudentRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRegistrationRepositoryTest {

    @Autowired
    private CourseRegistrationRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByStudent_shouldReturnRegistrations() {
        Student student = createAndSaveStudent("s1@test.com");
        Course course = createAndSaveCourse("Bazy danych");
        createAndSaveRegistration(student, course, "ACTIVE");

        List<CourseRegistration> result = repository.findByStudent(student);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourse().getName()).isEqualTo("Bazy danych");
    }

    @Test
    void findByStudentIdAndStatus_shouldReturnRegistrations() {
        Student student = createAndSaveStudent("s2@test.com");
        Course course = createAndSaveCourse("Bazy danych");
        createAndSaveRegistration(student, course, "ACTIVE");

        List<CourseRegistration> result = repository.findByStudentIdAndStatus(student.getUserId(), "ACTIVE");

        assertThat(result).hasSize(1);
    }

    @Test
    void findCoursesByStudentEmailAndStatus_shouldReturnCourses() {
        Student student = createAndSaveStudent("s3@test.com");
        Course course = createAndSaveCourse("Bazy danych");
        createAndSaveRegistration(student, course, "ACTIVE");

        List<Course> result = repository.findCoursesByStudentEmailAndStatus("s3@test.com", "ACTIVE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Bazy danych");
    }

    @Test
    void findByStudentEmailAndCourseId_shouldReturnRegistration() {
        Student student = createAndSaveStudent("s4@test.com");
        Course course = createAndSaveCourse("Bazy danych");
        createAndSaveRegistration(student, course, "done");

        CourseRegistration result = repository.findByStudentEmailAndCourseId("s4@test.com", course.getId());

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("done");
    }


    private Student createAndSaveStudent(String email) {
        Student s = new Student();
        s.setEmail(email);
        s.setPassword("pass");
        s.setFirstName("First");
        s.setLastName("Last");
        s.setLibraryCardNumber("123");
        s.setStatus("active");
        s.setStudentNumber("12345");
        return studentRepository.save(s);
    }

    private Course createAndSaveCourse(String name) {
        Course c = new Course();
        c.setName(name);
        c.setEcts(5);
        c.setStatus("opened");
        return courseRepository.save(c);
    }

    private CourseRegistration createAndSaveRegistration(Student s, Course c, String status) {
        CourseRegistration r = new CourseRegistration();
        r.setStudent(s);
        r.setCourse(c);
        r.setStatus(status);
        return repository.save(r);
    }
}