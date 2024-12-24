package mockcote.statservice.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import mockcote.statservice.dto.TagDisplayNameDto;
import mockcote.statservice.dto.TagDto;
import mockcote.statservice.dto.TagStatsResponseDto;
import mockcote.statservice.dto.UserTagStatsDto;

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



}
