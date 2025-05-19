package JustGrades.app.service;

import JustGrades.app.config.AuthHelper;
import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.*;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.StudentRepository;
import JustGrades.app.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    @Mock
    private CourseRegistrationRepository registrationRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AuthHelper authHelper;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
        GET COURSE REGISTERED
     */
    @Test
    void getCourseRegistered_shouldReturnList() {
        String email = "student@example.com";
        CourseRegisteredDTO dto = new CourseRegisteredDTO();
        User user = new User();
        user.setEmail(email);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(registrationRepository.findCoursesRegisteredByStudentEmail(email))
                .thenReturn(Collections.singletonList(dto));

        List<CourseRegisteredDTO> result = registrationService.getCourseRegistered();
        assertThat(result).hasSize(1);
        verify(registrationRepository).findCoursesRegisteredByStudentEmail(email);
    }
    @Test
    void registerForCourse_shouldThrow_whenCourseNotFound() {
        Long invalidCourseId = 999L;

        when(courseRepository.findById(invalidCourseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> registrationService.registerForCourse(invalidCourseId))
                .isInstanceOf(NoSuchElementException.class);
    }

    /*
        REGISTER FOR COURSE
     */
    @Test
    void registerForCourse_shouldSaveRegistration() {
        Long courseId = 1L;
        String email = "student@example.com";
        Course course = new Course();
        Student student = new Student();
        User user = new User();
        user.setEmail(email);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findByEmail(email)).thenReturn(student);
        when(registrationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CourseRegistration registration = registrationService.registerForCourse(courseId);

        assertThat(registration.getCourse()).isEqualTo(course);
        assertThat(registration.getStudent()).isEqualTo(student);
        assertThat(registration.getStatus()).isEqualTo("application submitted");
    }

    /*
        DEREGISTER FROM COURSE
     */
    @Test
    void deregisterFromCourse_shouldDeleteRegistration() {
        Long courseId = 1L;
        String email = "student@example.com";
        CourseRegistration reg = new CourseRegistration();
        User user = new User();
        user.setEmail(email);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(registrationRepository.findByStudentEmailAndCourseId(email, courseId)).thenReturn(reg);

        CourseRegistration result = registrationService.deregisterFromCourse(courseId);

        assertThat(result).isEqualTo(reg);
        verify(registrationRepository).delete(reg);
    }


    @Test
    void deregisterFromCourse_shouldThrowIfNotRegistered() {
        Long courseId = 2L;
        String email = "student@example.com";
        User user = new User();
        user.setEmail(email);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(registrationRepository.findByStudentEmailAndCourseId(email, courseId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> registrationService.deregisterFromCourse(courseId));
    }

    @Test
    void deregisterFromCourse_shouldNotAffectOthers() {
        Long courseId = 5L;

        CourseRegistration reg = new CourseRegistration();
        reg.setCourseRegId(1L);
        reg.setCourse(new Course());
        reg.setStudent(new Student());
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(Role.STUDENT);

        when(authHelper.getCurrentUser()).thenReturn(user);
        when(registrationRepository.findByStudentEmailAndCourseId("test@test.com", courseId)).thenReturn(reg);

        registrationService.deregisterFromCourse(courseId);

        verify(registrationRepository, times(1)).delete(reg);
        verify(registrationRepository, never()).deleteAll();
    }
}
