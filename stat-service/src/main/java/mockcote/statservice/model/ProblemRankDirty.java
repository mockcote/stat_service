package mockcote.statservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem_rank_dirty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRankDirty {

    @Id
    @Column(nullable = false)
    private Integer problemId;

    @Column(nullable = false)
    private Boolean dirty = true;
}
