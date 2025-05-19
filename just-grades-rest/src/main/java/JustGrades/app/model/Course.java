package JustGrades.app.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseSeq")
    @SequenceGenerator(name = "courseSeq", sequenceName = "COURSES_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "name is mandatory")
    private String name;

    @Column(name = "ects_points")
    @NotNull(message = "ects points are mandatory")
    private Integer ects;

    @Column(name = "status")
    private String status = "active";

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseId", cascade = CascadeType.ALL,  orphanRemoval = true)
    @NotNull(message = "at least one completion requirement is mandatory")
    private Set<CompletionRequirement> completionRequirements = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseId", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Specialization> specializations = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EnrollRequirement> enrollRequirements = new HashSet<>();

    public Course() {
    }

    public Course(String name, Integer ects) {
        this.name = name;
        this.ects = ects;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", ects=" + ects + ", completionRequirements="
                + completionRequirements + ", enrollRequirements=" + enrollRequirements + "]";
    }

}