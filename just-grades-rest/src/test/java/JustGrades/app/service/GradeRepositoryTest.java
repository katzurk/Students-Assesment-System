package JustGrades.app.service;

import JustGrades.app.model.*;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByStudentEmailAndCourseId_shouldReturnGrades() {
        Student student = new Student();
        student.setEmail("student@test.com");
        student.setStudentNumber("12345");
        student.setPassword("securepass123");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setLibraryCardNumber("LC123456");
        student.setStatus("ACTIVE");
        student = studentRepository.save(student);

        Course course = new Course();
        course.setName("Math");
        course.setEcts(4);
        course = courseRepository.save(course);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setGrade(5);
        gradeRepository.save(grade);

        List<Grade> grades = gradeRepository.findByStudentEmailAndCourseId("student@test.com", course.getId());

        assertThat(grades).hasSize(1);
        assertThat(grades.get(0).getGrade()).isEqualTo(5);
    }

    @Test
    void findByCourseIdOrderByStudentLastNameAsc_shouldReturnSortedGrades() {
        Course course = new Course();
        course.setName("Science");
        course.setEcts(4);
        course = courseRepository.save(course);

        Student s1 = new Student();
        s1.setLastName("Zebra");
        s1.setEmail("student@test.com");
        s1.setStudentNumber("100");
        s1.setPassword("securepass123");
        s1.setFirstName("John");
        s1.setLibraryCardNumber("LC123456");
        s1.setStatus("ACTIVE");
        s1 = studentRepository.save(s1);

        Student s2 = new Student();
        s2.setEmail("student2@test.com");
        s2.setLastName("Alpha");
        s2.setStudentNumber("101");
        s2.setPassword("securepass123");
        s2.setFirstName("John");
        s2.setLibraryCardNumber("LC123456");
        s2.setStatus("ACTIVE");
        s2 = studentRepository.save(s2);

        Grade g1 = new Grade();
        g1.setStudent(s1);
        g1.setCourse(course);
        g1.setGrade(3);
        gradeRepository.save(g1);

        Grade g2 = new Grade();
        g2.setStudent(s2);
        g2.setCourse(course);
        g2.setGrade(4);
        gradeRepository.save(g2);

        List<Grade> result = gradeRepository.findByCourseIdOrderByStudentLastNameAsc(course.getId());
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStudent().getLastName()).isEqualTo("Alpha");
        assertThat(result.get(1).getStudent().getLastName()).isEqualTo("Zebra");
    }

    @Test
    void getGradesCrossSection_shouldReturnEmptyListOrNotNull() {
        List<GradesCrossSectionDTO> result = gradeRepository.getGradesCrossSection(1, "FINAL");
        assertThat(result).isNotNull();
    }

    @Test
    void getFinalGradesDistribution_shouldReturnEmptyListOrNotNull() {
        List<FinalGradesDistributionDTO> result = gradeRepository.getFinalGradesDistribution(1);
        assertThat(result).isNotNull();
    }
}
