-- 1. mockcote_stat 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS mockcote_stat
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 2. mockcote_stat 데이터베이스 선택
USE mockcote_stat;

-- 3. histories 테이블 생성
CREATE TABLE IF NOT EXISTS histories (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 풀이 로그 고유 ID
    handle VARCHAR(50) NOT NULL,                  -- 사용자 고유 ID (외래 키)
    problem_id INT NOT NULL,                      -- 문제 ID (외래 키)
    status VARCHAR(20) NOT NULL,                  -- 풀이 상태 (예: SUCCESS, FAIL)
    start_time TIMESTAMP NOT NULL,                -- 문제 풀이 시작 시간
    limit_time INT NOT NULL,                      -- 풀이 제한 시간 (분 단위)
    duration INT NOT NULL,                        -- 풀이 소요 시간 (초 단위)
    language VARCHAR(50) NOT NULL                 -- 사용한 프로그래밍 언어
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. problem_rank 테이블 (각 문제별 상위 100명)
CREATE TABLE IF NOT EXISTS problem_rank (
    problem_id INT NOT NULL,               -- 문제 ID
    handle VARCHAR(50) NOT NULL,           -- 사용자 핸들
    duration INT NOT NULL,                 -- 소요 시간
    ranking INT NOT NULL,                     -- 랭킹
    PRIMARY KEY (problem_id, handle),      -- problem_id와 handle을 복합 기본 키로 설정
    INDEX idx_problem_handle (problem_id, handle) -- 데이터 정렬 최적화를 위한 index
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. total_rank 테이블 (전체 사용자 랭킹)
--    - 각 사용자별 누적 점수와 전체 랭킹 정보를 저장
CREATE TABLE IF NOT EXISTS total_rank (
    handle VARCHAR(50) NOT NULL PRIMARY KEY,  -- 핸들을 기본 키로 설정
    score INT NOT NULL,                       -- 사용자 점수
    ranking INT NOT NULL                         -- 전체 랭킹
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 6. problem_rank_dirty 테이블 (문제별 랭킹 갱신 여부)
--    - 각 문제별 랭킹 갱신이 필요하거나(Dirty) 완료되었는지(0 or 1)를 저장
CREATE TABLE IF NOT EXISTS problem_rank_dirty (
    problem_id INT NOT NULL PRIMARY KEY,  -- 문제 ID를 기본 키로 설정
    dirty TINYINT(1) NOT NULL DEFAULT 0  -- 0: 최신 상태, 1: 갱신 필요
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. user_stats 테이블 생성
CREATE TABLE IF NOT EXISTS user_stats (
    stat_id INT AUTO_INCREMENT PRIMARY KEY,             -- 통계 데이터 고유 ID
    handle VARCHAR(50) NOT NULL,                        -- 사용자 핸들 (ex: 백준 ID)
    total_problems INT DEFAULT 0,                       -- 총 문제 풀이 수
    solved_problems INT DEFAULT 0,                      -- 성공적으로 푼 문제 수
    failed_problems INT DEFAULT 0,                      -- 실패한 문제 수
    success_rate FLOAT DEFAULT 0.0,                     -- 성공률 (%)
    average_duration INT DEFAULT NULL,                  -- 평균 풀이 소요 시간 (초 단위)
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP                     -- 마지막 업데이트 시간
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;