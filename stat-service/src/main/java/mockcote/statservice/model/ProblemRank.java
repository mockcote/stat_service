package mockcote.statservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem_rank")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProblemRankId.class) // 복합 키 클래스 지정
public class ProblemRank {

    @Id
    @Column(nullable = false)
    private Integer problemId;

    @Id
    @Column(nullable = false)
    private String handle;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer ranking;
}
