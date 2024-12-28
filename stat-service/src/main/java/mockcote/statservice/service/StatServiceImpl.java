package mockcote.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.dto.UserTagStatsDto;
import mockcote.statservice.exception.CustomException;
import mockcote.statservice.model.Histories;
import mockcote.statservice.model.UserStats;
import mockcote.statservice.repository.HistoryRepository;
import mockcote.statservice.repository.UserStatsRepository;
import mockcote.statservice.util.LevelLoader;
import mockcote.statservice.util.SolvedAcApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

	private final HistoryRepository historyRepository;
	private final UserStatsRepository userStatsRepository;
	private final SolvedAcApiClient solvedAcApiClient;

	@Value("${app.target.tags}")
    private String targetTags;

	@Override
	@Transactional
	public void saveHistory(LogsRequest request) {

		// 히스토리 저장
		Histories history = new Histories();
		history.setHandle(request.getHandle());
		history.setProblemId(request.getProblemId());
		history.setStatus(request.getStatus());
		history.setStartTime(request.getStartTime());
		history.setLimitTime(request.getLimitTime());
		history.setDuration(request.getDuration());
		history.setLanguage(request.getLanguage());

		historyRepository.save(history);
		log.info("History saved");

		// 통계 업데이트 호출
		boolean isSolved = "SUCCESS".equals(request.getStatus());
		updateStats(request.getHandle(), isSolved, request.getDuration());
	}

	// 통계 업데이트
	@Transactional
	public void updateStats(String handle, boolean isSolved, int duration) {
		// 유저의 기존 통계 가져오기
		UserStats stats = userStatsRepository.findByHandle(handle)
				.orElseGet(() -> new UserStats(handle)); // 유저 통계가 없으면 기본값 생성

		// 총 문제 풀이 수 증가
		stats.setTotalProblems(stats.getTotalProblems() + 1);

		// 성공 여부
		if (isSolved) {
			// 해결한 문제 증가
			stats.setSolvedProblems(stats.getSolvedProblems() + 1);

			// 평균 소요 시간 재계산 (기존 평균값 + (새로운 값 - 기존 평균값)/해결한 문제 수)
			int solvedProblems = stats.getSolvedProblems(); // 해결한 문제 수
			int newAverageDuration = stats.getAverageDuration()
					+ (duration - stats.getAverageDuration()) / solvedProblems;
			stats.setAverageDuration(newAverageDuration);
		} else {
			// 실패 문제 증가
			stats.setFailedProblems(stats.getFailedProblems() + 1);
		}

		// 성공률 업데이트
		float successRate = ((float) stats.getSolvedProblems() / stats.getTotalProblems()) * 100;
		stats.setSuccessRate(successRate);

		// 통계 저장
		userStatsRepository.save(stats);
	}

	@Override
	public UserStats getUserStats(String handle) {
		UserStats userStats = userStatsRepository.findByHandle(handle)
				.orElseGet(() -> new UserStats(handle));
		return userStats;
	}

	@Override
	public List<UserTagStatsDto> getTagStats(String handle) {

		List<UserTagStatsDto> tagStats = null;
		try {
			// 태그별 통계 요청
			tagStats = solvedAcApiClient.fetchUserTagStats(handle);
		} catch (Exception e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "태그별 통계 가져오기 실패");
		}

		if (tagStats == null || tagStats.isEmpty()) {
			return List.of(); // 빈 리스트 반환
		}

		List<String> targetTagList = List.of(targetTags.split(","));

        return tagStats.stream()
                .filter(tag -> targetTagList.contains(tag.getTagName()))
                .sorted((o1,o2) -> o2.getSolvedCount() - o1.getSolvedCount())
                .collect(Collectors.toList());
	}

	@Override
	public List<LevelStatsResponse> getLevelStats(String handle) {
		List<LevelStatsResponse> response = solvedAcApiClient.fetchUserLevelStats(handle);
		for(LevelStatsResponse levelStats : response) {
			levelStats.setLevelName(LevelLoader.getLevelName(levelStats.getLevel()));
		}
		return response;
	}

}
