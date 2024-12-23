package mockcote.statservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.ProblemRankRequest;
import mockcote.statservice.dto.ProblemRankResponse;
import mockcote.statservice.service.ProblemRankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
}
