package mockcote.statservice.repository;

import mockcote.statservice.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Integer> {
    Optional<UserStats> findByHandle(String handle);
}
