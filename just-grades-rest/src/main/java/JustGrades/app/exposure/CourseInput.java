package JustGrades.app.exposure;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseInput {
    private Long id;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "ects points are mandatory")
    private Integer ects;

    @NotNull(message = "at least one completion requirement is mandatory")
    private List<CompletionRequirementInput> completionRequirements;

    private List<EnrollRequirementInput> enrollRequirements;

    public CourseInput() {
    }

    public CourseInput(String name, Integer ects) {
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

    public List<CompletionRequirementInput> getCompletionRequirements() {
        return completionRequirements;
    }

    public List<EnrollRequirementInput> getEnrollRequirements() {
        return enrollRequirements;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", ects=" + ects + ", completionRequirements="
                + completionRequirements + ", enrollRequirements=" + enrollRequirements + "]";
    }

}
