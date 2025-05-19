package JustGrades.app.controller;

import JustGrades.app.model.FinalGradesDistributionDTO;
import JustGrades.app.model.GradesCrossSectionDTO;
import JustGrades.app.services.GradeReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeReportController.class)
class GradeReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GradeReportService gradeReportService;

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getGradesCrossSection_shouldReturnList() throws Exception {
        GradesCrossSectionDTO dto1 = new GradesCrossSectionDTO(5, 3);
        GradesCrossSectionDTO dto2 = new GradesCrossSectionDTO(4, 5);
        when(gradeReportService.getGradesCrossSection(1, "FINAL")).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/course/1/gradereport/FINAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grade").value(5))
                .andExpect(jsonPath("$[0].studentsCount").value(3))
                .andExpect(jsonPath("$[1].grade").value(4))
                .andExpect(jsonPath("$[1].studentsCount").value(5));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getFinalGradesDistribution_shouldReturnList() throws Exception {
        FinalGradesDistributionDTO dto1 = new FinalGradesDistributionDTO("FINAL", 5);
        FinalGradesDistributionDTO dto2 = new FinalGradesDistributionDTO("TEST", 4);
        when(gradeReportService.getFinalGradesDistribution(1)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/course/1/coursereport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gradeStatus").value("FINAL"))
                .andExpect(jsonPath("$[1].studentsCount").value(4));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getGradesCrossSection_shouldReturnEmpty_whenNoData() throws Exception {
        when(gradeReportService.getGradesCrossSection(99, "MIDTERM")).thenReturn(List.of());

        mockMvc.perform(get("/course/99/gradereport/MIDTERM"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getFinalGradesDistribution_shouldReturnEmpty_whenNoData() throws Exception {
        when(gradeReportService.getFinalGradesDistribution(99)).thenReturn(List.of());

        mockMvc.perform(get("/course/99/coursereport"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}