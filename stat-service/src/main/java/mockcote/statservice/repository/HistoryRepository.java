package mockcote.statservice.repository;

import mockcote.statservice.model.Histories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<Histories, Long> {

    Page<?> findByHandle(String handle, Pageable pageable);
}
