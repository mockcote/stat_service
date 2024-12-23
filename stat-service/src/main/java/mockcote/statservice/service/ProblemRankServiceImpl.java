package mockcote.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.model.ProblemRank;
import mockcote.statservice.model.ProblemRankDirty;
import mockcote.statservice.model.ProblemRankDirtyRepository;
import mockcote.statservice.repository.ProblemRankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemRankServiceImpl implements ProblemRankService {

    private final ProblemRankRepository problemRankRepository;
    private final ProblemRankDirtyRepository problemRankDirtyRepository;

    @Override
    @Transactional
    public ProblemRankResponse updateProblemRank(ProblemRankRequest request) {
        Integer problemId = request.getProblemId();
        String handle = request.getHandle();
        Integer duration = request.getDuration();

        // 기존 데이터 가져오기
        List<ProblemRank> ranks = problemRankRepository.findByProblemIdOrderByDurationAsc(problemId);

        // 새로운 데이터 추가
        ProblemRank newRank = new ProblemRank();
        newRank.setProblemId(problemId);
        newRank.setHandle(handle);
        newRank.setDuration(duration);

        // 기존 목록에 새로운 데이터 추가
        ranks.add(newRank);

        // 랭킹 갱신 (duration 기준 정렬)
        ranks.sort((a, b) -> Integer.compare(a.getDuration(), b.getDuration()));

        // 상위 100명까지만 처리
        for (int i = 0; i < ranks.size(); i++) {
            ProblemRank rank = ranks.get(i);
            rank.setRanking(i + 1); // 1등부터 랭킹 설정
            problemRankRepository.save(rank); // 업데이트된 랭킹 저장
        }

        // Dirty Flag 갱신
        ProblemRankDirty dirtyEntry = problemRankDirtyRepository.findById(problemId)
                .orElse(new ProblemRankDirty());
        dirtyEntry.setProblemId(problemId);
        dirtyEntry.setDirty(true);
        problemRankDirtyRepository.save(dirtyEntry);

        // 현재 사용자의 랭킹 반환
        Integer userRank = ranks.stream()
                .filter(rank -> rank.getHandle().equals(handle))
                .map(ProblemRank::getRanking)
                .findFirst()
                .orElse(null);

        return new ProblemRankResponse(handle, problemId, userRank);
    }



}
