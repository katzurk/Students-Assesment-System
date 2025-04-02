package JustGrades.app.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name = "courses")
public class Course {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseSeq")
    @SequenceGenerator(name = "courseSeq", sequenceName = "COURSES_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "name is mandatory")
    private String name;

    @Column(name = "ects")
    @NotBlank(message = "ects points are mandatory")
    private Integer ects;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_req",
    joinColumns = { @JoinColumn(name = "courses_id") },
    inverseJoinColumns = { @JoinColumn(name = "req_id") })
    @NotNull(message = "at least one completion requirement is mandatory")
    private List<CompletionRequirement> completionRequirements;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollRequirement> enrollRequirements;

    public Course() {
    }

    public Course(String name, Integer ects) {
        this.name = name;
        this.ects = ects;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getEcts() {
        return this.ects;
    }


    public void setName(String new_name) {
        this.name = new_name;
    }

    public void setEcts(Integer new_ects) {
        this.ects = new_ects;
    }

    public List<CompletionRequirement> getCompletionRequirements() {
        return completionRequirements;
    }

    public List<EnrollRequirement> getEnrollRequirements() {
        return enrollRequirements;
    }

}
