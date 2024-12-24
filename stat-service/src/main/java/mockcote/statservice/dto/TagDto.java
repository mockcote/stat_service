package mockcote.statservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private String key;
    private boolean isMeta;
    private int bojTagId;
    private int problemCount;
    private List<TagDisplayNameDto> displayNames;
}
