package JustGrades.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Student", description = "Endpoints related to student data and grades")
@RestController
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

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
