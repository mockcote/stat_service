package mockcote.statservice.service;

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
}
