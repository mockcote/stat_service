package mockcote.statservice.repository;

import mockcote.statservice.model.ProblemRank;
import mockcote.statservice.model.ProblemRankId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRankRepository extends JpaRepository<ProblemRank, ProblemRankId> {
    List<ProblemRank> findByProblemIdOrderByDurationAsc(Integer problemId);
    void deleteByProblemId(Integer problemId);
    
    /**
     * 특정 문제 ID에 해당하는 모든 랭킹 데이터를 랭킹 순으로 정렬하여 반환합니다.
     *
     * @param problemId 문제 ID
     * @return 랭킹 리스트
     */
    List<ProblemRank> findByProblemIdOrderByRankingAsc(Integer problemId);
}
