package JustGrades.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lecturers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lecturer extends User{
    @Column(name = "lecture_id")
    @NotBlank(message = "id is mandatory")
    private Long lecturerId;

    @Column(name = "academic_title")
    private String academicTitle;
}