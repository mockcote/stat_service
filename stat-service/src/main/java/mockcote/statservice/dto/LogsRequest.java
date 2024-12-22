package mockcote.statservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsRequest {
	@NotBlank
    private String handle;
	
	@NotNull
    private Integer problemId;
	
	@NotNull
    private LocalDateTime startTime;
	
	@NotNull
    private Integer limitTime;
	
	@NotNull
    private Integer duration;
	
	@NotBlank
    private String language;
	
	@NotBlank
    private String status;
}