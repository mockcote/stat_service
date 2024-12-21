package mockcote.statservice.service;

import mockcote.statservice.dto.LogsRequest;

public interface StatService {
    /**
     * 로그 데이터를 저장
     * @param logsRequest
     */
    void saveHistory(LogsRequest logsRequest);
}
