package JustGrades.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NamedStoredProcedureQuery(
    name = "getGradesCrossSectionProcedure",
    procedureName = "get_grades_cross_section",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_course_id", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_grade_type", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor", type = void.class)
    },
    resultSetMappings = "GradeCrossSectionDTOMapping"
)
@SqlResultSetMapping(
    name = "GradeCrossSectionDTOMapping",
    classes = @ConstructorResult(
        targetClass = GradesCrossSectionDTO.class,
        columns = {
            @ColumnResult(name = "grade", type = Integer.class),
            @ColumnResult(name = "students_count", type = Integer.class)
        }
    )
)

@NamedStoredProcedureQuery(
    name = "getFinalGradesDistributionProcedure",
    procedureName = "get_final_grades_distribution",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_course_id", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor", type = void.class)
    },
    resultSetMappings = "FinalGradesDistributionDTOMapping"
)
@SqlResultSetMapping(
    name = "FinalGradesDistributionDTOMapping",
    classes = @ConstructorResult(
        targetClass = FinalGradesDistributionDTO.class,
        columns = {
            @ColumnResult(name = "grade_status", type = String.class),
            @ColumnResult(name = "students_count", type = Integer.class)
        }
    )
)

@Entity
@Table(name = "grades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grades_seq")
    @SequenceGenerator(name = "grades_seq", sequenceName = "GRADES_SEQ", allocationSize = 1)
    @Column(name = "grade_id")
    private Long id;

    @Column(name = "grade")
    @NotNull(message = "grade is mandatory")
    private Double grade;

    @Temporal(TemporalType.DATE)
    @Column(name = "received_date")
    private Date receivedDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @Column(name = "type", nullable = false)
    private String type;
}