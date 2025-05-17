package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "SPECIALIZATIONS")
public class Specialization {
    @Id
    @Column(name = "SPECIALIZATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specializationId;

    @Column(nullable = false, unique = true)
    private String name;
}