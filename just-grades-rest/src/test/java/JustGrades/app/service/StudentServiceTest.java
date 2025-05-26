package JustGrades.app.service;

import JustGrades.app.config.AuthHelper;
import JustGrades.app.model.*;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import JustGrades.app.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GradeRepository  gradeRepository;

    @Mock
    private CourseRegistrationRepository courseRegistrationRepository;

    @Mock
    private AuthHelper authHelper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
        Get FINAL GRADES
     */
    @Test
    void getFinalGrades_shouldReturnCorrectGradesMap() {

        User mockUser = new User();
        mockUser.setEmail("test@student.com");

        Student mockStudent = new Student();
        mockStudent.setUserId(1L);
        mockStudent.setEmail("test@student.com");

        Course course1 = new Course();
        course1.setName("Bazy Danych");
        course1.setId(101L);

        CourseRegistration reg1 = new CourseRegistration();
        reg1.setCourse(course1);

        when(authHelper.getCurrentUser()).thenReturn(mockUser);
        when(studentRepository.findByEmail("test@student.com")).thenReturn(mockStudent);
        when(courseRegistrationRepository.findByStudentIdAndStatus(1L, "FINISHED"))
                .thenReturn(List.of(reg1));
        when(studentRepository.getFinalGrade(1L, 101L)).thenReturn(Optional.of(4.5));


        Map<String, Double> result = studentService.getFinalGrades();

        assertEquals(1, result.size());
        assertEquals(4.5, result.get("Bazy Danych"));
    }

    @Test
    void getFinalGrades_shouldSkipIfNoGradeReturned() {
        User user = new User();
        user.setEmail("nograde@student.com");

        Student student = new Student();
        student.setUserId(3L);
        student.setEmail("nograde@student.com");

        Course course = new Course();
        course.setName("Systemy operacyjne");
        course.setId(201L);

        CourseRegistration reg = new CourseRegistration();
        reg.setCourse(course);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(studentRepository.findByEmail("nograde@student.com")).thenReturn(student);
        when(courseRegistrationRepository.findByStudentIdAndStatus(3L, "ACTIVE"))
                .thenReturn(List.of(reg));
        when(studentRepository.getFinalGrade(3L, 201L)).thenReturn(Optional.empty());

        Map<String, Double> grades = studentService.getFinalGrades();

        assertTrue(grades.isEmpty());
    }

    @Test
    void getFinalGrades_shouldHandleEmptyCourseList() {
        User user = new User();
        user.setEmail("empty@student.com");

        Student student = new Student();
        student.setUserId(2L);
        student.setEmail("empty@student.com");

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(studentRepository.findByEmail("empty@student.com")).thenReturn(student);
        when(courseRegistrationRepository.findByStudentIdAndStatus(2L, "ACTIVE"))
                .thenReturn(Collections.emptyList());

        Map<String, Double> grades = studentService.getFinalGrades();

        assertTrue(grades.isEmpty());
    }

    /*
        GET STUDENT INFO
     */

    @Test
    void getStudentInfo(){
        User mockUser = new User();
        mockUser.setEmail("test@student.com");

        Student mockStudent = new Student();
        mockStudent.setUserId(1L);
        mockStudent.setEmail(mockUser.getEmail());
        mockStudent.setStatus("ACTIVE");
        mockStudent.setLibraryCardNumber("L23421232");

        when(authHelper.getCurrentUser()).thenReturn(mockUser);
        when(studentRepository.findByEmail("test@student.com")).thenReturn(mockStudent);

        Student result = studentService.getStudentInfo();
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@student.com");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getLibraryCardNumber()).isEqualTo("L23421232");
    }

    @Test
    void getStudentInfo_shouldThrow_whenNotAuthenticated() {
        when(authHelper.getCurrentUser()).thenThrow(new IllegalStateException("No user logged in."));

        assertThatThrownBy(() -> studentService.getStudentInfo())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No user logged in.");
    }

    /*
        GET ALL STUDENT COURSES
     */

    @Test
    void getAllStudentCourses_shouldReturnCourses(){
        User mockUser = new User();
        mockUser.setEmail("test@student.com");

        Course course = new Course();
        course.setName("Math");

        Course course2 = new Course();
        course2.setName("BazyDanych");

        when(authHelper.getCurrentUser()).thenReturn(mockUser);
        when(courseRegistrationRepository.findCoursesByStudentEmailAndStatus("test@student.com", "ACTIVE"))
                .thenReturn(List.of(course, course2));

        List<Course> result = studentService.getAllStudentCourses();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Math");
        assertThat(result.get(1).getName()).isEqualTo("BazyDanych");
    }

    @Test
    void getAllStudentCourses_shouldReturnEmptyList() {
        User user = new User();
        user.setEmail("test@student.com");

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(courseRegistrationRepository.findCoursesByStudentEmailAndStatus("test@student.com", "ACTIVE"))
                .thenReturn(Collections.emptyList());

        List<Course> result = studentService.getAllStudentCourses();
        assertThat(result).isEmpty();
    }

    /*
        GET STUDENT GRADES BY COURSE
     */
    @Test
    void getStudentGradesByCourse_shouldReturnGrades() {
        User user = new User();
        user.setEmail("test@student.com");

        Grade grade = new Grade();
        grade.setGrade(4.0);

        Grade grade2 = new Grade();
        grade2.setGrade(3.0);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(gradeRepository.findByStudentEmailAndCourseId("test@student.com", 1L))
                .thenReturn(List.of(grade, grade2));

        List<Grade> result = studentService.getStudentGradesByCourse(1L);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGrade()).isEqualTo(4);
        assertThat(result.get(1).getGrade()).isEqualTo(3);
    }

    @Test
    void getStudentGradesByCourse_shouldReturnEmptyList() {
        User user = new User();
        user.setEmail("test@student.com");

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(gradeRepository.findByStudentEmailAndCourseId("test@student.com", 1L))
                .thenReturn(Collections.emptyList());

        List<Grade> result = studentService.getStudentGradesByCourse(1L);
        assertThat(result).isEmpty();
    }

    /*
        SAVE STUDENT
     */

    @Test
    void saveStudent_shouldCreateAndSaveStudentCorrectly() {

        User user = new User();
        user.setFirstName("Anna");
        user.setLastName("Nowak");
        user.setEmail("anna@test.com");
        user.setPassword("secret");
        user.setRole(Role.STUDENT);


        Student savedStudent = new Student();
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.saveStudent(user);


        assertThat(result).isNotNull();
        verify(studentRepository).save(any(Student.class));
    }


}
