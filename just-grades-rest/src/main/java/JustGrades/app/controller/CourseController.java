package JustGrades.app.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.exposure.CompletionRequirementInput;
import JustGrades.app.exposure.CourseInput;
import JustGrades.app.exposure.EnrollRequirementInput;
import JustGrades.app.model.CompletionRequirement;
import JustGrades.app.model.Course;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.model.EnrollRequirement;
import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Courses", description = "Endpoints related to course management")
@RestController
public class CourseController {
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Operation(summary = "Get all courses sorted by name (ascending)")
    @GetMapping("/courses")
    public List<Course> getCoursesSortedByNameAsc() {
        return courseRepository.findAllByOrderByNameAsc();
    }

    @Operation(summary = "Delete course by ID")
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable("id") long courseId) {
        try {
            courseRepository.deleteById(courseId);
            return ResponseEntity.ok().build();
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.warn("Error deleting course", e);
            return ResponseEntity.badRequest().body("Can not delete course, it is needed to complite another course");
        }
    }

    @Operation(summary = "Add a new course with its requirements")
    @PostMapping("/addcourse")
    public ResponseEntity<String> addCourse(@RequestBody @Valid CourseInput courseIn, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("-- add course has errors: " + result.getAllErrors());
            return ResponseEntity.badRequest().body("Form NOT submitted");
        }
        Course course = mapToCourse(courseIn);
        course = courseRepository.save(course);
        mapEnrollRequirements(courseIn, course);
        mapCompletionRequirements(courseIn, course);
        course = courseRepository.save(course);

        return ResponseEntity.ok("Form submitted successfully");
    }

        @PostMapping("/courses/{id}/open-registration")
    public ResponseEntity<String> openRegistration(@PathVariable("id") long courseId) {
        try {
            courseRepository.openRegistration(courseId);
            return ResponseEntity.ok("Registration opened for course " + courseId);
        } catch (Exception e) {
            logger.error("Failed to open registration", e);
            return ResponseEntity.badRequest().body("Error opening registration");

        }
    }

    @PostMapping("/courses/{id}/close-registration")
    public ResponseEntity<String> closeRegistration(@PathVariable("id") long courseId) {
        try {
            courseRepository.closeRegistration(courseId);
            return ResponseEntity.ok("Registration closed for course " + courseId);
        } catch (Exception e) {
            logger.error("Failed to close registration", e);
            return ResponseEntity.badRequest().body("Error closing registration");
        }
    }

    @PostMapping("/courses/{id}/close")
    public ResponseEntity<String> closeCourse(@PathVariable("id") long courseId) {
        try {
            System.out.println(courseId);
            courseRepository.closeCourse(courseId);
            return ResponseEntity.ok("Course closed with id " + courseId);
        } catch (Exception e) {
            logger.error("Failed to close course", e);
            return ResponseEntity.badRequest().body("Error closing course");
        }
    }

    @PostMapping("/courses/{id}/open-registration")
    public ResponseEntity<String> openRegistration(@PathVariable("id") long courseId) {
        try {
            courseRepository.openRegistration(courseId);
            return ResponseEntity.ok("Registration opened for course " + courseId);
        } catch (Exception e) {
            logger.error("Failed to open registration", e);
            return ResponseEntity.badRequest().body("Error opening registration");

        }
    }

    @PostMapping("/courses/{id}/close-registration")
    public ResponseEntity<String> closeRegistration(@PathVariable("id") long courseId) {
        try {
            courseRepository.closeRegistration(courseId);
            return ResponseEntity.ok("Registration closed for course " + courseId);
        } catch (Exception e) {
            logger.error("Failed to close registration", e);
            return ResponseEntity.badRequest().body("Error closing registration");
        }
    }

    @PostMapping("/courses/{id}/close")
    public ResponseEntity<String> closeCourse(@PathVariable("id") long courseId) {
        try {
            System.out.println(courseId);
            courseRepository.closeCourse(courseId);
            return ResponseEntity.ok("Course closed with id " + courseId);
        } catch (Exception e) {
            logger.error("Failed to close course", e);
            return ResponseEntity.badRequest().body("Error closing course");
        }
    }

    private Course mapToCourse(CourseInput courseIn) {
        Course c = new Course();
        c.setEcts(courseIn.getEcts());
        c.setName(courseIn.getName());
        return c;
    }

    private void mapEnrollRequirements(CourseInput courseIn, Course course) {
        for (EnrollRequirementInput enrIn : courseIn.getEnrollRequirements()) {
            EnrollRequirement enrReq = new EnrollRequirement(enrIn.getMinEcts(), course.getId());
            if (enrIn.getComplitedCourseId() != null) {
                Course reqCourse = courseRepository.findById(enrIn.getComplitedCourseId())
                    .orElseThrow(() -> new RuntimeException("Course with id: " + enrIn.getComplitedCourseId()
                    + " not found. Cannot be added as Enrolment requirement"));
                enrReq.setComplitedCourse(reqCourse);
            }
            course.getEnrollRequirements().add(enrReq);
        }
    }

    private void mapCompletionRequirements(CourseInput courseIn, Course course) {
        for (CompletionRequirementInput comIn : courseIn.getCompletionRequirements()) {
            CompletionRequirement comReq = new CompletionRequirement(comIn.getMinScore(), comIn.getType(), course.getId());
            course.getCompletionRequirements().add(comReq);
        }
    }
}
