package JustGrades.app.repository;

import JustGrades.app.model.FinalGradesDistributionDTO;
import JustGrades.app.model.Grade;
import JustGrades.app.model.GradesCrossSectionDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentEmailAndCourseId(String email, Long courseId);

    @Query("SELECT g FROM Grade g JOIN g.student s WHERE g.course.id = :courseId ORDER BY s.lastName")
    List<Grade> findByCourseIdOrderByStudentLastNameAsc(Long courseId);

    @Procedure(name = "getGradesCrossSectionProcedure", outputParameterName = "p_cursor", refCursor = true)
    List<GradesCrossSectionDTO> getGradesCrossSection(@Param("p_course_id") int courseId, @Param("p_grade_type") String gradeType);

    @Procedure(name = "getFinalGradesDistributionProcedure", outputParameterName = "p_cursor", refCursor = true)
    List<FinalGradesDistributionDTO> getFinalGradesDistribution(@Param("p_course_id") int courseId);

}
