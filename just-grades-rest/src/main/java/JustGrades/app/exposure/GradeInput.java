package JustGrades.app.exposure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import JustGrades.app.model.Course;
import JustGrades.app.model.Lecturer;
import JustGrades.app.model.Student;

@Getter
@Setter
public class GradeInput {
    private Long id;

    @NotBlank(message = "grade is mandatory")
    private Integer grade;

    @NotBlank(message = "type is mandatory")
    private String type;

    private Student student;

    private Course course;

    private Lecturer lecturer;

}