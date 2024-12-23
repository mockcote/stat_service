package mockcote.statservice.service;

import java.util.List;

public interface ProblemTagStatsService {
    List<Integer> getLowestFiveTags(String handle);
}
