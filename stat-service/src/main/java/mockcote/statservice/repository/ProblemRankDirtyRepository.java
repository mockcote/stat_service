package mockcote.statservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mockcote.statservice.model.ProblemRankDirty;

public interface ProblemRankDirtyRepository extends JpaRepository<ProblemRankDirty, Integer> {
	
	/**
     * Dirty 상태인 문제를 반환합니다.
     *
     * @param dirty Dirty 상태 여부
     * @return Dirty 상태인 문제 목록
     */
    List<ProblemRankDirty> findByDirty(boolean dirty);
}
