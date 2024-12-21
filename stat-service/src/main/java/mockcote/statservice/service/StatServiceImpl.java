package mockcote.statservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mockcote.statservice.dto.LogsRequest;
import mockcote.statservice.model.Histories;
import mockcote.statservice.repository.HistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final WebClient webClient;
    private final HistoryRepository historyRepository;

    @Override
    @Transactional
    public void saveHistory(LogsRequest request) {

        // 히스토리 저장
        Histories history = new Histories();
        history.setHandle(request.getHandle());
        history.setProblemId(request.getProblemId());
        history.setStatus(request.getStatus());
        history.setStartTime(request.getStartTime());
        history.setLimitTime(request.getLimitTime());
        history.setDuration(request.getDuration());
        history.setLanguage(request.getLanguage());

        historyRepository.save(history);
        log.info("History saved");
    }
}
