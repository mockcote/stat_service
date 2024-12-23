package mockcote.statservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "total_rank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalRank {

    @Id
    private String handle; // 사용자 ID (Primary Key)

    @Column(nullable = false)
    private Integer score = 0; // 사용자 점수, 기본값 0

    @Column(nullable = false)
    private Integer ranking = 0; // 사용자 랭킹, 기본값 0
}

