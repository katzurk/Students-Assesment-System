package JustGrades.app.controller;

import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import JustGrades.app.services.RegistrationService;
import JustGrades.app.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import JustGrades.app.model.Course;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class RegistrationsController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("course/register")
    public ResponseEntity<String> registerForCourse(@RequestParam("courseId") Long courseId) {
        registrationService.registerForCourse(courseId);
        return ResponseEntity.ok("Registered successfully.");
    }

    @GetMapping("course/deregister")
    public ResponseEntity<String> deregisterFromCourse(@RequestParam("courseId") Long courseId) {
        registrationService.deregisterFromCourse(courseId);
        return ResponseEntity.ok("Deregistered successfully.");
    }
}
