package JustGrades.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GradesCrossSectionDTO {
    private int grade;
    private int studentsCount;

    public GradesCrossSectionDTO(int grade, int studentsCount) {
        this.grade = grade;
        this.studentsCount = studentsCount;
    }
}

