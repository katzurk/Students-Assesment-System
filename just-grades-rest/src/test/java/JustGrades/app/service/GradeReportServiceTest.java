package JustGrades.app.service;

import JustGrades.app.model.FinalGradesDistributionDTO;
import JustGrades.app.model.GradesCrossSectionDTO;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.services.GradeReportService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GradeReportServiceTest {
    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeReportService gradeReportService;

    public GradeReportServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    /*
        GET GRADES CROSS-SECTION
     */

    @Test
    void getGradesCrossSection_shouldReturnList() {
        GradesCrossSectionDTO dto1 = new GradesCrossSectionDTO();
        GradesCrossSectionDTO dto2 = new GradesCrossSectionDTO();

        when(gradeRepository.getGradesCrossSection(1, "FINAL")).thenReturn(List.of(dto1, dto2));

        List<GradesCrossSectionDTO> result = gradeReportService.getGradesCrossSection(1, "FINAL");

        assertEquals(2, result.size());
        verify(gradeRepository, times(1)).getGradesCrossSection(1, "FINAL");
    }
    @Test
    void getGradesCrossSection_shouldReturnEmptyList_whenNoData() {
        when(gradeRepository.getGradesCrossSection(999, "MIDTERM")).thenReturn(List.of());

        List<GradesCrossSectionDTO> result = gradeReportService.getGradesCrossSection(999, "MIDTERM");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gradeRepository).getGradesCrossSection(999, "MIDTERM");
    }
    @Test
    void getGradesCrossSection_shouldPropagateException() {
        when(gradeRepository.getGradesCrossSection(anyInt(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            gradeReportService.getGradesCrossSection(1, "final");
        });
    }

    /*
        GET FINAL GRADES DISTRIBUTION
     */
    @Test
    void getFinalGradesDistribution_shouldReturnList() {
        FinalGradesDistributionDTO dto = new FinalGradesDistributionDTO();
        when(gradeRepository.getFinalGradesDistribution(1)).thenReturn(List.of(dto));

        List<FinalGradesDistributionDTO> result = gradeReportService.getFinalGradesDistribution(1);

        assertEquals(1, result.size());
        verify(gradeRepository, times(1)).getFinalGradesDistribution(1);
    }
    @Test
    void getFinalGradesDistribution_shouldPropagateException() {
        when(gradeRepository.getFinalGradesDistribution(anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            gradeReportService.getFinalGradesDistribution(1);
        });
    }
}
