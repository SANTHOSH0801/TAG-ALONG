package com.tagalong.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tagalong.modal.LoginLog;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    List<LoginLog> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    default LoginLog getLastLogin(String userId) {
        List<LoginLog> list = findByUserIdOrderByCreatedAtDesc(userId, Pageable.ofSize(1));
        return list.isEmpty() ? null : list.get(0);
    }
    
}
