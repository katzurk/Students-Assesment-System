package JustGrades.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.model.GradesCrossSectionDTO;
import JustGrades.app.services.GradeReportService;

@RestController
public class GradeReportController {

    private final GradeReportService gradeReportService;

    @Autowired
    public GradeReportController(GradeReportService gradeReportService) {
        this.gradeReportService = gradeReportService;
    }

    @GetMapping("/course/{courseId}/gradereport/{gradeType}")
    public List<GradesCrossSectionDTO> getReport(
            @PathVariable int courseId,
            @PathVariable String gradeType) {
        return gradeReportService.getGradesCrossSection(courseId, gradeType);
    }
}

