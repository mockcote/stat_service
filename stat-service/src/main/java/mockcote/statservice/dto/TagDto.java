package mockcote.statservice.dto;

import java.util.List;

public class TagDto {
    private String key;
    private boolean isMeta;
    private int bojTagId;
    private int problemCount;
    private List<TagDisplayNameDto> displayNames;

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isMeta() {
        return isMeta;
    }

    public void setMeta(boolean meta) {
        isMeta = meta;
    }

    public int getBojTagId() {
        return bojTagId;
    }

    public void setBojTagId(int bojTagId) {
        this.bojTagId = bojTagId;
    }

    public int getProblemCount() {
        return problemCount;
    }

    public void setProblemCount(int problemCount) {
        this.problemCount = problemCount;
    }

    public List<TagDisplayNameDto> getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(List<TagDisplayNameDto> displayNames) {
        this.displayNames = displayNames;
    }
}
