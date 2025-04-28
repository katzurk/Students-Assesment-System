package JustGrades.app.repository;

import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;
import JustGrades.app.model.GradesCrossSectionDTO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long>{
    List<Grade> findByStudentEmailAndCourseId(String email, Long courseId);

    List<Grade> findByCourseIdOrderByStudentLastNameAsc(Long courseId);

    @Procedure(procedureName = "get_grades_cross_section", outputParameterName = "p_cursor", refCursor = true)
    // List<GradeCrossSectionDTO> getGradesCrossSection(@Param("p_course_id") int courseId, @Param("p_grade_type") String gradeType);
    List<Object> getGradesCrossSection(@Param("p_course_id") int courseId, @Param("p_grade_type") String gradeType);

    @Procedure(procedureName = "get_final_grades_distribution", outputParameterName = "p_cursor", refCursor = true)
    List<Object> getFinalGradesDistribution(@Param("p_course_id") int courseId);

}
