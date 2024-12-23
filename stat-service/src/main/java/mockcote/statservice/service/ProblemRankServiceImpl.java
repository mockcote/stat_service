package mockcote.statservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.model.ProblemRank;
import mockcote.statservice.model.ProblemRankDirty;
import mockcote.statservice.model.TotalRank;
import mockcote.statservice.repository.ProblemRankDirtyRepository;
import mockcote.statservice.repository.ProblemRankRepository;
import mockcote.statservice.repository.TotalRankRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemRankServiceImpl implements ProblemRankService {

	private final ProblemRankDirtyRepository problemRankDirtyRepository;
    private final ProblemRankRepository problemRankRepository;
    private final TotalRankRepository totalRankRepository;

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

    @Override
    public List<ProblemRankResponse> getProblemRanks(Integer problemId) {
        // 데이터베이스에서 문제 ID에 해당하는 모든 랭킹 조회
        List<ProblemRank> ranks = problemRankRepository.findByProblemIdOrderByRankingAsc(problemId);

        // ProblemRank 엔티티를 ProblemRankResponse로 변환
        return ranks.stream()
                .map(rank -> new ProblemRankResponse(
                        rank.getHandle(),
                        rank.getProblemId(),
                        rank.getRanking()))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void updateTotalRank() {
        // 1. Dirty = 1인 문제 가져오기
        List<ProblemRankDirty> dirtyProblems = problemRankDirtyRepository.findByDirty(true);

        // 2. 사용자 점수 계산용 Map 초기화
        Map<String, Integer> userScores = new HashMap<>();

        // 3. 각 문제의 랭킹 점수를 계산
        for (ProblemRankDirty dirtyProblem : dirtyProblems) {
            Integer problemId = dirtyProblem.getProblemId();
            List<ProblemRank> ranks = problemRankRepository.findByProblemIdOrderByRankingAsc(problemId);

            for (ProblemRank rank : ranks) {
                int score = 101 - rank.getRanking(); // 1등은 100점, 100등은 1점
                userScores.put(rank.getHandle(), userScores.getOrDefault(rank.getHandle(), 0) + score);
            }

            // Dirty 플래그를 0으로 갱신
            dirtyProblem.setDirty(false);
            problemRankDirtyRepository.save(dirtyProblem);
        }

        // 4. TotalRank 테이블 갱신
        for (Map.Entry<String, Integer> entry : userScores.entrySet()) {
            String handle = entry.getKey();
            int score = entry.getValue();

            TotalRank totalRank = totalRankRepository.findById(handle).orElse(new TotalRank());
            totalRank.setHandle(handle);
            totalRank.setScore(totalRank.getScore() + score); // 기존 점수에 새로운 점수 추가
            totalRankRepository.save(totalRank);
        }

        // 5. 전체 사용자 점수를 기반으로 랭킹 갱신
        List<TotalRank> allRanks = totalRankRepository.findAllByOrderByScoreDesc();
        for (int i = 0; i < allRanks.size(); i++) {
            TotalRank rank = allRanks.get(i);
            rank.setRanking(i + 1); // 1등부터 랭킹 설정
            totalRankRepository.save(rank); // 랭킹 갱신 후 저장
        }
    }


}
