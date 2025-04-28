package JustGrades.app.model;

import java.math.BigDecimal;

import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Entity;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.ParameterMode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @SqlResultSetMappings(
//     name = "GradeCrossSectionDTOMapping",
//     classes = @ConstructorResult(
//         targetClass = GradeCrossSectionDTO.class,
//         columns = {
//             @ColumnResult(name = "grade", type = String.class),
//             @ColumnResult(name = "students_count", type = Integer.class)
//         }
//     )
// )
// @NamedStoredProcedureQuery(
//     name = "getGradesCrossSection",
//     procedureName = "get_grades_cross_section",
//     parameters = {
//         @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_course_id", type = Integer.class),
//         @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_grade_type", type = String.class),
//         @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_cursor", type = java.sql.ResultSet.class)
//     },
//     resultSetMappings = "GradeCrossSectionDTOMapping"
// )
// @Entity

@Getter
@Setter
@NoArgsConstructor
public class FinalGradesDistributionDTO {
    private String gradeStatus;
    private BigDecimal studentsCount;

    public FinalGradesDistributionDTO(String gradeStatus, BigDecimal studentsCount) {
        this.gradeStatus = gradeStatus;
        this.studentsCount = studentsCount;
    }
}
