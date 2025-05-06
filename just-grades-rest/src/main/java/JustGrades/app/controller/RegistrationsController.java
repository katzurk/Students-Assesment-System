package JustGrades.app.controller;

import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.Course;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Registrations", description = "Endpoints for course registration and deregistration")
@RestController
@AllArgsConstructor
public class RegistrationsController {
    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "Get list of opened courses with registration status")
    @GetMapping("/registration")
    public List<CourseRegisteredDTO> getAllOpenedCourses() {
        return registrationService.getCourseRegistered();
    }

    @Operation(summary = "Register the current user for a selected course")
    @PostMapping("/registration/register")
    public ResponseEntity<String> registerForCourse(@RequestParam("courseId") Long courseId) {
        registrationService.registerForCourse(courseId);
        return ResponseEntity.ok("Registered successfully.");
    }

    @Operation(summary = "Deregister the current user from a selected course")
    @PostMapping("/registration/deregister")
    public ResponseEntity<String> deregisterFromCourse(@RequestParam("courseId") Long courseId) {
        registrationService.deregisterFromCourse(courseId);
        return ResponseEntity.ok("Deregistered successfully.");
    }
}
