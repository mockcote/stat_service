package mockcote.statservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.service.StatService;
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
     * 태그별 문제 풀이 통계 API
     */
    @GetMapping("/tags")
    public ResponseEntity<?> getTagStats(@RequestParam String handle) {
        List<?> tagStats = statService.getTagStats(handle);
        return ResponseEntity.ok(tagStats); // 결과 반환
    }

    /**
     * 난이도별 문제 풀이 통계 API
     */
    @GetMapping("/levels")
    public ResponseEntity<?> getLevelStats(@RequestParam String handle) {
        List<LevelStatsResponse> levelStats = statService.getLevelStats(handle);
        return ResponseEntity.ok(levelStats); // 결과 반환
    }

}
