package JustGrades.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import JustGrades.app.model.GradesCrossSectionDTO;
import JustGrades.app.repository.GradeRepository;

@Service
public class GradeReportService {
    @Autowired
    private GradeRepository repository;

    @Transactional
    public List<GradesCrossSectionDTO> getGradesCrossSection(int courseId, String gradeType) {
        return repository.getGradesCrossSection(courseId, gradeType);
    }
}
