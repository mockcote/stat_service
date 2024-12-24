package mockcote.statservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LevelStatsResponse;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.dto.TagStatsResponse;
import mockcote.statservice.service.StatService;

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
    public ResponseEntity<List<TagStatsResponse>> getTagStats(@RequestParam String handle) {
        log.info("Fetching tag stats for handle: {}", handle); // 요청 로그
    	List<TagStatsResponse> tagStats = statService.getTagStats(handle);
        return ResponseEntity.ok(tagStats); // 결과 반환
    }

    /**
     * 난이도별 문제 풀이 통계 API
     */
    @GetMapping("/levels")
    public ResponseEntity<List<LevelStatsResponse>> getLevelStats(@RequestParam String handle) {
        log.info("Fetching level stats for handle: {}", handle); // 요청 로그
    	List<LevelStatsResponse> levelStats = statService.getLevelStats(handle);
        return ResponseEntity.ok(levelStats); // 결과 반환
    }

}
