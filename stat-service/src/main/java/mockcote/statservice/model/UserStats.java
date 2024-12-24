package mockcote.statservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_stats")
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statId;

    @Column(nullable = false, unique = true)
    private String handle;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int totalProblems;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int solvedProblems;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int failedProblems;

    @Column(nullable = false, columnDefinition = "float default 0.0")
    private float successRate;

    @Column
    private Integer averageDuration;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Timestamp lastUpdated;

    public UserStats(String handle) {
        this.handle = handle;
        this.totalProblems = 0;
        this.solvedProblems = 0;
        this.failedProblems = 0;
        this.successRate = 0.0f;
        this.averageDuration = 0;
    }
}
