package mockcote.statservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProblemRankId implements Serializable {
    private Integer problemId;
    private String handle;
}
