package mockcote.statservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.model.UserStats;
import mockcote.statservice.service.StatService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatController {

    private final StatService statService;

    // 새로운 로그를 히스토리에 저장
    @PostMapping("/history")
    public ResponseEntity<?> saveHistory(@RequestBody @Valid LogsRequest request) {
        statService.saveHistory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 사용자 히스토리 조회
     *
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @RequestParam @NotBlank String handle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size ) {
        Page<?> historyPage = statService.getHistory(handle, page, size);
        return ResponseEntity.ok(historyPage); // Page 객체 반환
    }

    /**
     * 사용자 통계
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam @NotBlank String handle) {
        UserStats userStats = statService.getUserStats(handle);
        return ResponseEntity.ok(userStats);
    }

    /**
     * 태그별 문제 풀이 통계 API
     */
    @GetMapping("/tags")
    public ResponseEntity<?> getTagStats(@RequestParam @NotBlank String handle) {
        List<?> tagStats = statService.getTagStats(handle);
        return ResponseEntity.ok(tagStats); // 결과 반환
    }

    /**
     * 난이도별 문제 풀이 통계 API
     */
    @GetMapping("/levels")
    public ResponseEntity<?> getLevelStats(@RequestParam @NotBlank String handle) {
        List<LevelStatsResponse> levelStats = statService.getLevelStats(handle);
        return ResponseEntity.ok(levelStats); // 결과 반환
    }

}
