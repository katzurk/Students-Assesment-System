package JustGrades.app.exposure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import JustGrades.app.model.Lecturer;

@Getter
@Setter
public class GradeInput {
    private Long id;

    @NotNull(message = "grade is mandatory")
    private Integer grade;

    @NotBlank(message = "type is mandatory")
    private String type;

    private Lecturer lecturer;

    private String studentNumber;

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String new_studentNumber) {
        this.studentNumber = new_studentNumber;
    }

    @Override
    public String toString() {
        return "GradeInput [id=" + id + ", grade=" + grade + ", type=" + type + ", studentNumber=" + studentNumber
                + "]";
    }


}