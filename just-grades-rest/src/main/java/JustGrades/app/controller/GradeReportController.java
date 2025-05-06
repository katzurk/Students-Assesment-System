package JustGrades.app.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import JustGrades.app.model.FinalGradesDistributionDTO;
import JustGrades.app.model.GradesCrossSectionDTO;
import JustGrades.app.services.GradeReportService;

@Tag(name = "Grade Reports", description = "Endpoints for generating grade-related reports")
@RestController
public class GradeReportController {

    private final GradeReportService gradeReportService;

    @Autowired
    public GradeReportController(GradeReportService gradeReportService) {
        this.gradeReportService = gradeReportService;
    }

    @Operation(summary = "Get cross-section report for specific grade type in a course")
    @GetMapping("/course/{courseId}/gradereport/{gradeType}")
    public List<GradesCrossSectionDTO> getReport(
            @PathVariable int courseId,
            @PathVariable String gradeType) {
        return gradeReportService.getGradesCrossSection(courseId, gradeType);
    }

    @Operation(summary = "Get final grade distribution report for a course")
    @GetMapping("/course/{courseId}/coursereport")
    public List<FinalGradesDistributionDTO> getReport(@PathVariable int courseId) {
        return gradeReportService.getFinalGradesDistribution(courseId);
    }
}

