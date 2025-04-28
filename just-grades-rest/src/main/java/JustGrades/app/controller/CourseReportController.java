package JustGrades.app.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.model.FinalGradesDistributionDTO;
import JustGrades.app.services.CourseReportService;


@RestController
public class CourseReportController {

    private final CourseReportService courseReportService;

    @Autowired
    public CourseReportController(CourseReportService courseReportService) {
        this.courseReportService = courseReportService;
    }

    @GetMapping("/course/{courseId}/coursereport")
    public Set<FinalGradesDistributionDTO> getReport(
            @PathVariable int courseId) {
        return courseReportService.getFinalGradesDistribution(courseId);
    }
}
