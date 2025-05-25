package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classes")
@Getter
@Setter
public class Classes {
    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classSeq")
    @SequenceGenerator(name = "classSeq", sequenceName = "CLASS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "course_id")
    public Long courseId;
}
