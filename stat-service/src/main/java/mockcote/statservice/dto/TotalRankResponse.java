package mockcote.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalRankResponse {
    private String handle; // 사용자 ID
    private Integer score; // 사용자 점수
    private Integer ranking; // 사용자 랭킹
}
