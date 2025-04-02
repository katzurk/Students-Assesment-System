package JustGrades.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.ui.Model;

import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRepository;
import jakarta.validation.Valid;

@RestController
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

    // @GetMapping("/addcourse")
    // public String showAddCottageForm(Model model, Course course) {
    //     // model.addAttribute("cities", cityRepository.findAll());
    //     return;
    // }

    // @PostMapping("/addcottage")
    // public String addCottage(@Valid Cottage cottage, BindingResult result, Model model, @SessionAttribute("loggedInUser") User login) {
    //     if (result.hasErrors()) {
    //         logger.warn("-- add cottage has errors: " + result.getAllErrors());
    //         return "add-cottage";
    //     }
    //     cottage.setOwner(login);
    //     cottageRepository.save(cottage);
    //     return "redirect:/my-cottages";
    // }
}
