package JustGrades.app.exposure;

public class EnrollRequirementInput {
    private Integer minEcts;
    private Long complitedCourseId;

    public EnrollRequirementInput() {
    }

    public Integer getMinEcts() {
        return minEcts;
    }

    public void setMinEcts(Integer minEcts) {
        this.minEcts = minEcts;
    }

    public Long getComplitedCourseId() {
        return complitedCourseId;
    }

    public void setComplitedCourseId(Long complitedCourseId) {
        this.complitedCourseId = complitedCourseId;
    }

    @Override
    public String toString() {
        return "EnrollRequirementInput [minEcts=" + minEcts + ", complitedCourseId=" + complitedCourseId + "]";
    }

}