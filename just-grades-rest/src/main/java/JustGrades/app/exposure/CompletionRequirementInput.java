package JustGrades.app.exposure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompletionRequirementInput {
    @NotNull(message = "min_score is mandatory")
    private int minScore;

    @NotBlank(message = "type is mandatory")
    private String type;

    public CompletionRequirementInput() {
    }

    public CompletionRequirementInput(int minScore, String type) {
        this.minScore = minScore;
        this.type = type;
    }

    public int getMinScore() {
        return this.minScore;
    }

    public String getType() {
        return this.type;
    }

    public void setMinScore(int newMinScore) {
        this.minScore = newMinScore;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    @Override
    public String toString() {
        return "CompletionRequirementInput [minScore=" + minScore + ", type=" + type + "]";
    }

}
