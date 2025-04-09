package JustGrades.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User{

    @Column(name = "student_number")
    private String studentNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "library_card_number")
    private String libraryCardNumber;
}
