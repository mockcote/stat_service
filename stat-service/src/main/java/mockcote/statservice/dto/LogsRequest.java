package mockcote.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsRequest {
    private String handle;
    private Integer problemId;
    private LocalDateTime startTime;
    private Integer limitTime;
    private Integer duration;
    private String language;
    private String status;
}