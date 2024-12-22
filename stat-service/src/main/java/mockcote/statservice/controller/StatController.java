package mockcote.statservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LogsRequest;
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
}
