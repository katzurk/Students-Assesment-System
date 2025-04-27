package JustGrades.app.controller;

import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.Course;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RegistrationsController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registration")
    public List<CourseRegisteredDTO> getAllOpenedCourses() {
        return registrationService.getCourseRegistered();
    }

    @PostMapping("/registration/register")
    public ResponseEntity<String> registerForCourse(@RequestParam("courseId") Long courseId) {
        registrationService.registerForCourse(courseId);
        return ResponseEntity.ok("Registered successfully.");
    }

    @PostMapping("/registration/deregister")
    public ResponseEntity<String> deregisterFromCourse(@RequestParam("courseId") Long courseId) {
        registrationService.deregisterFromCourse(courseId);
        return ResponseEntity.ok("Deregistered successfully.");
    }
}
