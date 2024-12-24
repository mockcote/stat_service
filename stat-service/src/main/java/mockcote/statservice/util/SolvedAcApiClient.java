package mockcote.statservice.util;

import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.*;
import mockcote.statservice.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SolvedAcApiClient {

    private final WebClient webClient;

    public SolvedAcApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://solved.ac/api/v3").build();
    }

    public List<UserTagStatsDto> fetchUserTagStats(String handle) {
        String url = String.format("/user/problem_tag_stats?handle=%s", handle);

        try {
            TagStatsResponseDto response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(TagStatsResponseDto.class)
                    .block();

            if (response == null || response.getItems() == null) {
                System.err.println("API response or items is null.");
                return List.of(); // 빈 리스트 반환
            }

            return response.getItems().stream()
                    .map(item -> {
                        TagDto tag = item.getTag();
                        return new UserTagStatsDto(
                                tag.getDisplayNames().stream()
                                        .filter(name -> "ko".equals(name.getLanguage()))
                                        .findFirst()
                                        .map(TagDisplayNameDto::getName)
                                        .orElse("unknown"),
                                tag.getBojTagId(),
                                item.getSolved()
                        );
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Failed to fetch tag stats: " + e.getMessage());
            return List.of();
        }
    }

    // 난이도별 풀이 수 가져오기
    public List<LevelStatsResponse> fetchUserLevelStats(String handle) {
        String url = String.format("/user/problem_stats?handle=%s", handle);
        List<LevelStatsResponse> response = null;

        try {
            response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(LevelStatsResponse[].class)
                .map(List::of)
                .block();
        } catch (Exception e) {
            log.error("Failed to fetch level stats: {}", e.getMessage());
            return List.of();
        }

        if (response == null || response.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        return response;
    }
}
