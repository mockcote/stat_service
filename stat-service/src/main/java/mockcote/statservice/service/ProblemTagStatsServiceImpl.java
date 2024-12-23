package mockcote.statservice.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import mockcote.statservice.dto.UserTagStatsDto;
import mockcote.statservice.util.SolvedAcApiClient;

@Service
public class ProblemTagStatsServiceImpl implements ProblemTagStatsService {

    private final SolvedAcApiClient apiClient;

    public ProblemTagStatsServiceImpl(SolvedAcApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<Integer> getLowestFiveTags(String handle) {
        List<UserTagStatsDto> tagStats = apiClient.fetchUserTagStats(handle);

        if (tagStats == null || tagStats.isEmpty()) {
            System.err.println("Tag stats are empty or null.");
            return List.of(); // 빈 리스트 반환
        }

        List<String> targetTags = List.of(
            "자료 구조", "그래프 이론", "그래프 탐색", "너비 우선 탐색", "구현",
            "다이나믹 프로그래밍", "브루트포스 알고리즘", "문자열", "정렬",
            "그리디 알고리즘", "깊이 우선 탐색", "시뮬레이션", "백트래킹", "이분 탐색",
            "세그먼트 트리", "정수론", "최단 경로", "트리", "비트마스킹",
            "데이크스트라", "해시를 사용한 집합과 맵", "사칙연산", "누적 합",
             "조합론", "분리 집합", "스위핑", "트리를 사용한 집합과 맵"
        );

        return tagStats.stream()
                .filter(tag -> targetTags.contains(tag.getTagName()))
                .sorted(Comparator.comparingInt(UserTagStatsDto::getSolvedCount))
                .limit(5)
                .map(UserTagStatsDto::getBojTagId)
                .collect(Collectors.toList());
    }

}
