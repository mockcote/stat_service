package mockcote.statservice.repository;

import mockcote.statservice.model.TotalRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TotalRankRepository extends JpaRepository<TotalRank, String> {

    /**
     * 전체 사용자를 점수 기준으로 내림차순 정렬하여 반환합니다.
     *
     * @return 사용자 랭킹 리스트
     */
    List<TotalRank> findAllByOrderByScoreDesc();
    
    /**
     * 전체 사용자의 점수와 랭킹 정보를 랭킹 순으로 정렬하여 반환합니다.
     * @return 전체 사용자 랭킹 정보 리스트
     */
    List<TotalRank> findAllByOrderByRankingAsc();

}
