package JustGrades.app.controller;

import JustGrades.app.config.SecurityConfig;
import JustGrades.app.model.Grade;
import JustGrades.app.model.User;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentCourseRepository;
import JustGrades.app.repository.UserRepository;
import jakarta.servlet.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import JustGrades.app.model.Course;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @GetMapping("/student")
    public User getStudentInfo() {
        return userRepository.findUserByUserId(1L); // later change to current user
    }

    @GetMapping("/student/courses")
    public List<Course> getAllStudentCourses() {
        return studentCourseRepository.findByStudentUserId(1L);
    }

    @GetMapping("/student/courses/{courseId}")
    public List<Grade> getStudentCourseGrades(@PathVariable Long courseId) {
        return gradeRepository.findByStudentUserIdAndCourseId(1L, courseId);
    }
}
