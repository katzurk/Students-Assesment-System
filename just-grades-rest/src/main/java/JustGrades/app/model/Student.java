package JustGrades.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("STUDENT")
public class Student extends User {
    @Column(name = "student_number")
    @NotBlank(message = "student number is mandatory")
    private String studentNumber;

    @Column(name = "status")
    @NotBlank(message = "status is mandatory")
    private String status;

    @Column(name = "library_card_number")
    @NotBlank(message = "value is mandatory")
    private String libraryCardNumber;

    @Column (name = "specialization")
    private String specializationName;

    @Transient
    @Enumerated(EnumType.STRING)
    protected Role role = Role.STUDENT;
}
