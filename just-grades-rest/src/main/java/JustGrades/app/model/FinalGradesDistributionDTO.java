package JustGrades.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FinalGradesDistributionDTO {
    private String gradeStatus;
    private int studentsCount;

    public FinalGradesDistributionDTO(String gradeStatus, int studentsCount) {
        this.gradeStatus = gradeStatus;
        this.studentsCount = studentsCount;
    }
}
