package mockcote.statservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRankRequest {
    @NotBlank
    private String handle;

    @NotNull
    private Integer problemId;

    @NotNull
    private Integer duration;
}
