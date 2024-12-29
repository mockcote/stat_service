package mockcote.statservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Histories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id") // 테이블의 history_id와 매핑
    private Long id;

    @Column(nullable = false)
    private String handle;

    @Column(nullable = false)
    private Integer problemId;

    @Column(nullable = false, length = 20)
    private String status; // SUCCESS, FAIL

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer limitTime; // 분 단위 제한시간

    @Column(nullable = false)
    private Integer duration; // 초 단위 소요시간

    @Column(nullable = false, length = 20)
    private String language;
}
