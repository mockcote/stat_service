package mockcote.statservice.dto;

import lombok.Data;

@Data
public class TagStatsResponse {
    private int bojTagId; // 태그 ID
    private int solved;   // 해결한 문제 수
}
