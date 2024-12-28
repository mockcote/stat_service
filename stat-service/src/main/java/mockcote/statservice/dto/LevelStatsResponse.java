package mockcote.statservice.dto;

import lombok.Data;

@Data
public class LevelStatsResponse {
    private int level;    // 난이도
    private String levelName;
    private int solved;   // 해결한 문제 수
}
