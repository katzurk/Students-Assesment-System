package JustGrades.app.services;

import JustGrades.app.config.AuthHelper;
import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final CourseRegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AuthHelper authHelper;

    public List<CourseRegisteredDTO> getCourseRegistered() {
        return registrationRepository.findCoursesRegisteredByStudentEmail(authHelper.getCurrentUser().getEmail());
    }

    public CourseRegistration registerForCourse(Long courseId) {
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setCourse(courseRepository.findById(courseId).get());
        courseRegistration.setStudent(studentRepository.findByEmail(authHelper.getCurrentUser().getEmail()));
        courseRegistration.setStatus("application submitted");
        return registrationRepository.save(courseRegistration);
    }

    public CourseRegistration deregisterFromCourse(Long courseId) {
        CourseRegistration courseRegistration = registrationRepository.findByStudentEmailAndCourseId(authHelper.getCurrentUser().getEmail(), courseId);
        if (courseRegistration != null) {
            registrationRepository.delete(courseRegistration);
            return courseRegistration;
        } else {
            throw new EntityNotFoundException("You are not registered for this course");
        }
    }
}
