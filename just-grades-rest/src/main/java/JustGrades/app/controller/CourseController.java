package JustGrades.app.controller;


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CourseController {
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

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

    @PostMapping("/addcourse")
    public ResponseEntity<String> addCourse(@RequestBody @Valid CourseInput courseIn, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("-- add course has errors: " + result.getAllErrors());
            return ResponseEntity.badRequest().body("Form NOT submitted");
        }
        Course course = mapToCourse(courseIn);
        course = courseRepository.save(course);
        mapEnrollRequirements(courseIn, course);
        course = courseRepository.save(course);

        return ResponseEntity.ok("Form submitted successfully");
    }

    private Course mapToCourse(CourseInput courseIn) {
        Course c = new Course();
        c.setEcts(courseIn.getEcts());
        c.setName(courseIn.getName());
        for (CompletionRequirementInput compReq : courseIn.getCompletionRequirements()) {
            c.getCompletionRequirements().add(new CompletionRequirement(compReq.getMinScore(), compReq.getType()));
        }
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
}
