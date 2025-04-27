package JustGrades.app.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import JustGrades.app.controller.CourseController;
import JustGrades.app.model.GradeCrossSectionDTO;
import JustGrades.app.repository.GradeRepository;
import oracle.jdbc.OracleTypes;

import java.math.BigDecimal;
import java.sql.Types;

@Service
public class GradeReportService {
    Logger logger = LoggerFactory.getLogger(GradeReportService.class);

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private GradeRepository repository;

    @Autowired
    public GradeReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Set<GradeCrossSectionDTO> getGradesCrossSection(int courseId, String gradeType) {
        // SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        //     .withCatalogName("ADMIN")
        //     .withProcedureName("get_grades_cross_section")
        //     .declareParameters(
        //         new SqlParameter("p_course_id", OracleTypes.NUMBER),
        //         new SqlParameter("p_grade_type", Types.VARCHAR),
        //         new SqlOutParameter("p_cursor", OracleTypes.CURSOR, (rs, rowNum) ->
        //             new GradeCrossSectionDTO(
        //                 rs.getInt("grade"),
        //                 rs.getInt("students_count")
        //             )
        //         )
        //     );

        // MapSqlParameterSource params = new MapSqlParameterSource()
        //     .addValue("p_course_id", courseId)
        //     .addValue("p_grade_type", gradeType);

        // Map<String, Object> result = jdbcCall.execute(params);

        // @SuppressWarnings("unchecked")
        // List<GradeCrossSectionDTO> gradesList = (List<GradeCrossSectionDTO>) result.get("p_cursor");

        // return new HashSet<>(gradesList);
        List<Object> result = repository.getGradesCrossSection(courseId, gradeType);
        logger.info("============ precedure result: " + result);

        // return new HashSet<>();
        return Set.of(new GradeCrossSectionDTO(new BigDecimal(5), new BigDecimal(77)), new GradeCrossSectionDTO(new BigDecimal(3), new BigDecimal(33)),  new GradeCrossSectionDTO(new BigDecimal(4), new BigDecimal(65)));

    }
}
