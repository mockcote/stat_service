package mockcote.statservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.dto.TotalRankResponse;
import mockcote.statservice.exception.CustomException;
import mockcote.statservice.model.ProblemRank;
import mockcote.statservice.model.ProblemRankDirty;
import mockcote.statservice.model.TotalRank;
import mockcote.statservice.model.UserStats;
import mockcote.statservice.repository.ProblemRankDirtyRepository;
import mockcote.statservice.repository.ProblemRankRepository;
import mockcote.statservice.repository.TotalRankRepository;
import mockcote.statservice.repository.UserStatsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemRankServiceImpl implements ProblemRankService {

	private final ProblemRankDirtyRepository problemRankDirtyRepository;
    private final ProblemRankRepository problemRankRepository;
    private final TotalRankRepository totalRankRepository;
    private final UserStatsRepository userStatsRepository;

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
        // 1. 모든 problem_rank 데이터를 가져옵니다.
        List<ProblemRank> allProblemRanks = problemRankRepository.findAll();

        // 2. 사용자별로 그룹화하여 문제를 통해 얻은 점수를 계산합니다.
        Map<String, Integer> problemScores = allProblemRanks.stream()
                .collect(Collectors.groupingBy(
                        ProblemRank::getHandle,
                        Collectors.summingInt(rank -> 101 - rank.getRanking()) // 1등은 100점, 2등은 99점, ..., 100등은 1점
                ));

        // 3. user_stats에서 각 사용자의 total_problems 값을 가져옵니다.
        List<UserStats> allUserStats = userStatsRepository.findAll();
        Map<String, Integer> userTotalProblems = allUserStats.stream()
                .collect(Collectors.toMap(
                        UserStats::getHandle,
                        UserStats::getTotalProblems,
                        (existing, replacement) -> existing // 중복 시 기존 값을 유지
                ));

        // 4. 사용자별 총 점수 계산 (문제 점수 + total_problems)
        Map<String, Integer> totalUserScores = new HashMap<>();
        for (UserStats userStats : allUserStats) {
            String handle = userStats.getHandle();
            int problemScore = problemScores.getOrDefault(handle, 0);
            int totalProblems = userStats.getTotalProblems();
            totalUserScores.put(handle, problemScore + totalProblems);
        }

        // 5. total_rank 테이블을 업데이트합니다.
        for (Map.Entry<String, Integer> entry : totalUserScores.entrySet()) {
            String handle = entry.getKey();
            int totalScore = entry.getValue();

            TotalRank totalRank = totalRankRepository.findById(handle).orElse(new TotalRank());
            totalRank.setHandle(handle);
            totalRank.setScore(totalScore);
            totalRankRepository.save(totalRank);
        }

        // 6. 전체 랭킹을 갱신합니다.
        List<TotalRank> allRanks = totalRankRepository.findAllByOrderByScoreDesc();
        for (int i = 0; i < allRanks.size(); i++) {
            TotalRank rank = allRanks.get(i);
            rank.setRanking(i + 1); // 1등부터 순위 설정
            totalRankRepository.save(rank);
        }

        log.info("Total rankings have been updated successfully.");
    }



    
    
    @Override
    @Transactional
    public void incrementUserScoreAndRecalculateRankings(String handle) {
        // 1. total_rank에서 handle 조회 또는 초기화
        TotalRank totalRank = totalRankRepository.findById(handle).orElse(new TotalRank());
        if (totalRank.getHandle() == null) {
            totalRank.setHandle(handle);
            totalRank.setScore(0); // 초기화 점수
            totalRank.setRanking(0); // 초기화 랭킹
        }

        // 2. 점수 증가
        totalRank.setScore(totalRank.getScore() + 1);
        totalRankRepository.save(totalRank);

        // 3. 전체 랭킹 갱신
        List<TotalRank> allRanks = totalRankRepository.findAllByOrderByScoreDesc();
        for (int i = 0; i < allRanks.size(); i++) {
            TotalRank rank = allRanks.get(i);
            rank.setRanking(i + 1); // 1등부터 순서대로 랭킹 갱신
            totalRankRepository.save(rank);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public TotalRankResponse getUserRankInfo(String handle) {
        TotalRank totalRank = totalRankRepository.findById(handle)
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return new TotalRankResponse(totalRank.getHandle(), totalRank.getScore(), totalRank.getRanking());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TotalRankResponse> getAllUserRankInfo(Pageable pageable) {
        Page<TotalRank> allRanks = totalRankRepository.findAllByOrderByRankingAsc(pageable);

        // TotalRank를 TotalRankResponse로 변환
        return allRanks.map(rank -> new TotalRankResponse(rank.getHandle(), rank.getScore(), rank.getRanking()));
    }

}
