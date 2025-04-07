package JustGrades.app.controller;

import JustGrades.app.config.SecurityConfig;
import JustGrades.app.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import JustGrades.app.model.Course;

import java.util.Collections;
import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping("/student-courses")
    public List<Course> getAllStudentCourses() {
        String email = SecurityConfig.getCurrentUserName();

        if (email != null) {
            System.out.println(email);
            return gradeRepository.findByStudentEmail(email);
        }

        return Collections.emptyList();
    }

    @GetMapping("/student-courses/grades/{courseId}")
    public Course getStudentCourseGrades(@PathVariable Long courseId) {
        String email = SecurityConfig.getCurrentUserName();

        if (email != null) {
            System.out.println(email);
            return gradeRepository.findByStudentEmailCourseId(email, courseId);
        }

        return null;
    }
}
