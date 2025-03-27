package JustGrades.app.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "courses")
public class Course {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseSeq")
    @SequenceGenerator(name = "courseSeq", sequenceName = "COURSES_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "name")
    @NotBlank(message = "name is mandatory")
    private String name;

    @Column(name = "ects")
    @NotBlank(message = "ects points are mandatory")
    private int ects;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_req",
        joinColumns = { @JoinColumn(name = "courses_id") },
        inverseJoinColumns = { @JoinColumn(name = "req_id") })
    private List<CompletionRequirement> completionRequirements;

    public Course() {
    }

    public Course(String name, int ects) {
        this.name = name;
        this.ects = ects;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getEcts() {
        return this.ects;
    }


    public void setName(String new_name) {
        this.name = new_name;
    }

    public void setEcts(int new_ects) {
        this.ects = new_ects;
    }

    public List<CompletionRequirement> getCompletionRequirements() {
        return completionRequirements;
    }

}
