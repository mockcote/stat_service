package mockcote.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTagStatsDto {
    private String tagName;
    private int bojTagId;
    private int solvedCount;
}
