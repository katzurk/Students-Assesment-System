package JustGrades.app.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.exposure.GradeInput;
import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import jakarta.validation.Valid;

@RestController
public class GradeController {
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/course/{id}/grades")
    public List<Grade> getGradesList(@PathVariable("id") long courseId) {
        return gradeRepository.findByCourseIdOrderByStudentLastNameAsc(courseId);
    }

    @DeleteMapping("/course/{courseId}/grades/{gradeId}")
    public ResponseEntity<String> deleteGrade(@PathVariable("courseId") long courseId, @PathVariable("gradeId") long gradeId) {
        try {
            gradeRepository.deleteById(gradeId);
            return ResponseEntity.ok().build();
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.warn("Error deleting grade", e);
            return ResponseEntity.badRequest().body("Error deleting grade");
        }
    }

    @PostMapping("/course/{courseId}/addgrades")
    public ResponseEntity<String> addGrade(@RequestBody @Valid GradeInput gradeIn, @PathVariable("courseId") Long courseId, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("-- add grade has errors: " + result.getAllErrors());
            return ResponseEntity.badRequest().body("Form NOT submitted");
        }
        Grade grade = mapToGrade(gradeIn, courseId);
        grade = gradeRepository.save(grade);

        return ResponseEntity.ok("Form submitted successfully");
    }

    private Grade mapToGrade(GradeInput gradeIn, Long courseId) {
        Grade grade = new Grade();
        grade.setType(gradeIn.getType());
        grade.setGrade(gradeIn.getGrade());

        logger.info("----------- input grade: " + gradeIn);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isEmpty()){
            Course course = optionalCourse.get();
            grade.setCourse(course);
        }

        Student student = studentRepository.findStudentByStudentNumber(gradeIn.getStudentNumber());
        grade.setStudent(student);
        return grade;
    }
}
