package mockcote.statservice.service;

import java.util.List;

import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.dto.UserTagStatsDto;
import mockcote.statservice.model.UserStats;

public interface StatService {
	/**
	 * 로그 데이터를 저장
	 * 
	 * @param logsRequest
	 */
	void saveHistory(LogsRequest logsRequest);

	/**
	 * 태그별 통계 가져오기
	 *
	 * @param handle
	 * @return
	 */
	List<UserTagStatsDto> getTagStats(String handle);

	/**
	 * 난이도별 통계 가져오기
	 * @param handle
	 * @return
	 */
	List<LevelStatsResponse> getLevelStats(String handle);

	/**
	 * 유저 통계 가져오기
	 * @param handle
	 * @return
	 */
	UserStats getUserStats(String handle);
}
