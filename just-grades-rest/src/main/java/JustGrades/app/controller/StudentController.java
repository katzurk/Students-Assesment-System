package JustGrades.app.controller;

import JustGrades.app.config.AuthHelper;
import JustGrades.app.config.SecurityConfig;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.model.User;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import JustGrades.app.repository.UserRepository;
import JustGrades.app.services.StudentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import JustGrades.app.model.Course;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class StudentController {

    private final GradeRepository gradeRepository;


    private final StudentRepository studentRepository;

    private final StudentService studentService;


    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @GetMapping("/student-info")
    public Student getStudentInfo() {
        return studentRepository.findStudentByUserId(1002L); // later change to current user
    }


    @GetMapping("/student-info/courses")
    public List<Course> getAllStudentCourses() {
        return courseRegistrationRepository.findCoursesByStudentId(1002L);
    }

    @GetMapping("/student-info/courses/{courseId}")
    public List<Grade> getStudentGradesByCourse(@PathVariable Long courseId) {
        return gradeRepository.findByStudentUserIdAndCourseId(1002L, courseId);
    }

    @GetMapping("/student-info/final-grades")
    public Map<String, Double> getAllStudentFinalGrades() {
        System.out.println("Controller correct!!!");
        return studentService.getFinalGrades();
    }
}
