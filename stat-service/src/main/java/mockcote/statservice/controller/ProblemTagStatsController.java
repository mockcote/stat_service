package mockcote.statservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mockcote.statservice.service.ProblemTagStatsService;

@RestController
@RequestMapping("/stats")
public class ProblemTagStatsController {

    private final ProblemTagStatsService tagStatsService;

    public ProblemTagStatsController(ProblemTagStatsService tagStatsService) {
        this.tagStatsService = tagStatsService;
    }

    @GetMapping("/tags/lowest")
    public ResponseEntity<List<Integer>> getLowestTags(@RequestParam String handle) {
        List<Integer> lowestTags = tagStatsService.getLowestFiveTags(handle);
        return ResponseEntity.ok(lowestTags);
    }
}