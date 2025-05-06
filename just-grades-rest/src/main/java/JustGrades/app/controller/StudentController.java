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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import JustGrades.app.model.Course;

import java.util.List;
import java.util.Map;

@Tag(name = "Student", description = "Endpoints related to student data and grades")
@RestController
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;


    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Operation(summary = "Get information about the currently logged-in student")
    @GetMapping("/student-info")
    public Student getStudentInfo() {
        return studentService.getStudentInfo();
    }

    @Operation(summary = "Get all courses registered by the student")
    @GetMapping("/student-info/courses")
    public List<Course> getAllStudentCourses() {
        return studentService.getAllStudentCourses();
    }

    @Operation(summary = "Get all grades for the student in a specific course")
    @GetMapping("/student-info/grades/{courseId}")
    public List<Grade> getStudentGradesByCourse(@PathVariable Long courseId) {
        return studentService.getStudentGradesByCourse(courseId);
    }

    @Operation(summary = "Get final grades (average per course) for the student")
    @GetMapping("/student-info/final-grades")
    public Map<String, Double> getAllStudentFinalGrades() {
        return studentService.getFinalGrades();
    }
}
