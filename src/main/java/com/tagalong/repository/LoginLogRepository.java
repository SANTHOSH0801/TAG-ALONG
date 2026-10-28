package com.tagalong.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tagalong.modal.LoginLog;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    List<LoginLog> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    default LoginLog getLastLogin(String userId) {
        List<LoginLog> list = findByUserIdOrderByCreatedAtDesc(userId, Pageable.ofSize(1));
        return list.isEmpty() ? null : list.get(0);
    }
    
    @Query (
        value = 
            "SELECT COUNT(*) FROM ds.login_log WHERE user_id = :userUid AND created_at >= TO_TIMESTAMP(:cutoffTimeMs / 1000) "
            + "AND id > (SELECT COALESCE(MAX(id), 0) FROM ds.login_log WHERE user_id = :userUid "
            + "AND (login_status = 'SUCCESS' OR lock_status = 'RESET') AND is_deleted = false)"
            + "AND login_status = 'FAILED' AND is_deleted = false",
    nativeQuery = true)
    int consecutiveFailureCountForIncorrectPassword(
        @Param ("userUid") String userUid, @Param("cutoffTimeMs") Long cuttOffTimeMs
    );

    
    
}
