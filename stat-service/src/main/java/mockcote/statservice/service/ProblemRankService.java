package mockcote.statservice.service;

import java.util.List;

import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;

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
}
