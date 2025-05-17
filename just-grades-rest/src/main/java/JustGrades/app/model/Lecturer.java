package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("LECTURER")
public class Lecturer extends User {
    @Column(name = "ACADEMIC_TITLE")
    String academic_title;

    @Transient
    @Enumerated(EnumType.STRING)
    protected Role role = Role.LECTURER;
}
