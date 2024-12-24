package mockcote.statservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.dto.TotalRankResponse;

public interface ProblemRankService {
    /**
     * 특정 문제의 랭킹을 업데이트하고 현재 사용자의 랭킹을 반환합니다.
     *
     * @param request ProblemRankRequest (handle, problemId, duration)
     * @return ProblemRankResponse (handle, problemId, rank)
     */
    ProblemRankResponse updateProblemRank(ProblemRankRequest request);
    
    /**
     * 특정 문제의 전체 랭킹을 반환합니다.
     *
     * @param problemId 문제 ID
     * @return 전체 랭킹 리스트
     */
    List<ProblemRankResponse> getProblemRanks(Integer problemId);
    
    /**
     * 전체 사용자 점수와 랭킹을 갱신합니다.
     */
    void updateTotalRank();
    
    /**
     * 특정 사용자의 점수를 1 증가시키고, 전체 랭킹을 다시 계산합니다.
     * @param handle 사용자 ID
     */
    void incrementUserScoreAndRecalculateRankings(String handle);

    /**
     * 특정 사용자의 score와 ranking 정보를 조회합니다.
     * @param handle 사용자 ID
     * @return 사용자 랭킹 정보
     */
    TotalRankResponse getUserRankInfo(String handle);

    /**
     * 전체 사용자의 score와 ranking 정보를 조회합니다.
     * @return 전체 사용자 랭킹 정보 리스트
     */
    Page<TotalRankResponse> getAllUserRankInfo(Pageable pageable);

}
