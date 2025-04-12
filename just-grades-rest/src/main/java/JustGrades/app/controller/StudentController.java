package JustGrades.app.controller;

import JustGrades.app.model.Grade;
import JustGrades.app.model.User;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.UserRepository;
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

    @GetMapping("/student-info")
    public User getStudentInfo() {
        return userRepository.findUserByUserId(1L); // later change to current user
    }

    @GetMapping("/student-info/courses")
    public List<Course> getAllStudentCourses() {
        return gradeRepository.findByStudentId(1L);
    }

    @GetMapping("/student-info/courses/{courseId}")
    public List<Grade> getStudentGradesByCourse(@PathVariable Long courseId) {
        return gradeRepository.findByStudentUserIdAndCourseId(1L, courseId);
    }
}
