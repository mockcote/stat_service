package mockcote.statservice.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mockcote.statservice.dto.UserTagStatsDto;
import mockcote.statservice.util.SolvedAcApiClient;

@Service
@RequiredArgsConstructor
public class ProblemTagStatsServiceImpl implements ProblemTagStatsService {

    private final SolvedAcApiClient apiClient;

    @Value("${app.target.tags}")
    private String targetTags;

    @Override
    public List<Integer> getLowestFiveTags(String handle) {
        List<UserTagStatsDto> tagStats = apiClient.fetchUserTagStats(handle);

        if (tagStats == null || tagStats.isEmpty()) {
            System.err.println("Tag stats are empty or null.");
            return List.of(); // 빈 리스트 반환
        }

        List<String> targetTagList = List.of(targetTags.split(","));

        return tagStats.stream()
                .filter(tag -> targetTagList.contains(tag.getTagName()))
                .sorted(Comparator.comparingInt(UserTagStatsDto::getSolvedCount))
                .limit(5)
                .map(UserTagStatsDto::getBojTagId)
                .collect(Collectors.toList());
    }

}
