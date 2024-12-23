package mockcote.statservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.service.ProblemRankService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats/rank")
public class ProblemRankController {

    private final ProblemRankService problemRankService;

    @PostMapping("/problem")
    public ResponseEntity<ProblemRankResponse> updateAndFetchRank(@RequestBody @Valid ProblemRankRequest request) {
        ProblemRankResponse response = problemRankService.updateProblemRank(request);
        return ResponseEntity.ok(response);
    }
    
 // 특정 문제의 전체 랭킹 조회
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<ProblemRankResponse>> getProblemRank(@PathVariable Integer problemId) {
        List<ProblemRankResponse> ranks = problemRankService.getProblemRanks(problemId);
        return ResponseEntity.ok(ranks);
    }
    
 // 전체 사용자 랭킹 갱신
    @PostMapping("/total")
    public ResponseEntity<?> updateTotalRank() {
    	problemRankService.updateTotalRank();
        return ResponseEntity.ok().build();
    }
    
 // 사용자 점수 1 증가
    @PostMapping("/increment-score")
    public ResponseEntity<?> incrementUserScore(@RequestParam String handle) {
    	problemRankService.incrementUserScoreAndRecalculateRankings(handle);
        return ResponseEntity.ok().build();
    }

}
