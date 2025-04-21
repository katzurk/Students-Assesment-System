package JustGrades.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "course_requirement",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "completion_req_id")
    )
    @NotNull(message = "at least one completion requirement is mandatory")
    private List<CompletionRequirement> completionRequirements = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "courseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollRequirement> enrollRequirements = new ArrayList<>();

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