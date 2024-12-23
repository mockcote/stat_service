package mockcote.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRankResponse {
    private String handle;
    private Integer problemId;
    private Integer rank;
}
