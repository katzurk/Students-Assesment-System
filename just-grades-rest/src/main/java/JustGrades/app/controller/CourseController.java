package JustGrades.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRepository;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }
}
