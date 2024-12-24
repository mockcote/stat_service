package mockcote.statservice.dto;

public class UserTagStatsDto {
    private String tagName;
    private int bojTagId;
    private int solvedCount;

    public UserTagStatsDto(String tagName, int bojTagId, int solvedCount) {
        this.tagName = tagName;
        this.bojTagId = bojTagId;
        this.solvedCount = solvedCount;
    }

    // Getters and Setters
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getBojTagId() {
        return bojTagId;
    }

    public void setBojTagId(int bojTagId) {
        this.bojTagId = bojTagId;
    }

    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }
}
