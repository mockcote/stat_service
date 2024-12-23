package mockcote.statservice.repository;

import mockcote.statservice.model.ProblemRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRankRepository extends JpaRepository<ProblemRank, Long> {
    List<ProblemRank> findByProblemIdOrderByDurationAsc(Integer problemId);
    void deleteByProblemId(Integer problemId);
}
