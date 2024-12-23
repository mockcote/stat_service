package mockcote.statservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.dto.TagStatsResponse;
import mockcote.statservice.model.Histories;
import mockcote.statservice.model.UserStats;
import mockcote.statservice.repository.HistoryRepository;
import mockcote.statservice.repository.UserStatsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

	private final WebClient webClient;
	private final HistoryRepository historyRepository;
	private final UserStatsRepository userStatsRepository;

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
		UserStats stats = userStatsRepository.findByHandle(handle).orElseGet(() -> new UserStats(handle)); // 유저 통계가 없으면
																											// 기본값 생성

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

	@SuppressWarnings("unchecked")	// List<Map<String, Object> items... 라인 unchecked cast warning 때문에 일단 억
	@Override
	public List<TagStatsResponse> getTagStats(String handle) {
	    try {
	        WebClient webClient = WebClient.create();

	        // API 호출의 응답에서 items 추출
	        List<Map<String, Object>> items = (List<Map<String, Object>>) webClient.get()
	            .uri(uriBuilder -> uriBuilder
	                .scheme("https")
	                .host("solved.ac")
	                .path("/api/v3/user/problem_tag_stats")
	                .queryParam("handle", handle)
	                .build())
	            .retrieve() // 요청 실행
	            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}) // 응답 본문을 Map<String, Object>로 매핑
	            .block() // 비동기 처리 결과를 동기로 변환
	            .get("items"); // Map에서 "items" 키에 해당하는 값을 추출

	     // items가 null일 경우 빈 리스트 반환
	        if (items == null) {
	            return new ArrayList<>();
	        }

	     // "items" 리스트를 순회하며 TagStatsResponse 객체로 매핑
	        return items.stream()
	            .map(item -> {// 각 item을 처리
	                Map<String, Object> tag = (Map<String, Object>) item.get("tag");
	                int bojTagId = (int) tag.get("bojTagId"); // Extract "bojTagId"
	                int solved = (int) item.get("solved");   // Extract "solved"

	             // TagStatsResponse 객체 생성 및 데이터 설정
	                TagStatsResponse response = new TagStatsResponse();
	                response.setBojTagId(bojTagId);
	                response.setSolved(solved);
	                return response;// TagStatsResponse 반환
	            })
	            .toList();// 스트림 결과를 리스트로 변환

	    } catch (WebClientResponseException e) {
	    	// WebClient 요청 실패 시 발생하는 예외 처리
	        log.error("Response error: Status - {}, Body - {}", e.getStatusCode(), e.getResponseBodyAsString());
	        throw e;
	    } catch (Exception e) {
	    	// 기타 예외 처리
	        log.error("Error fetching tag stats", e);
	        throw e;
	    }
	}





	@Override
	public List<LevelStatsResponse> getLevelStats(String handle) {
	    try {
	    		    	
	        WebClient webClient = WebClient.create(); // 기본 WebClient 생성
	        List<LevelStatsResponse> response = webClient.get()
	                .uri(uriBuilder -> uriBuilder
	                    .scheme("https")
	                    .host("solved.ac")
	                    .path("/api/v3/user/problem_stats")
	                    .queryParam("handle", handle)
	                    .build())
	                .retrieve()
	                .bodyToMono(LevelStatsResponse[].class)
	                .doOnSuccess(res -> log.info("Level Stats Response: {}", (Object) res))
	                .doOnError(error -> log.error("Error fetching level stats: {}", error.getMessage()))
	                .map(List::of)
	                .block();

	        return response != null ? response : new ArrayList<>();
	    } catch (WebClientResponseException e) {
	        log.error("Response error: Status - {}, Body - {}", e.getStatusCode(), e.getResponseBodyAsString());
	        throw e;
	    } catch (Exception e) {
	        log.error("Error fetching level stats", e);
	        throw e;
	    }
	}

	

}
