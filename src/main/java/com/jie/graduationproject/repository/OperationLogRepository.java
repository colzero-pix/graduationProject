package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    List<OperationLog> findTop10ByOrderByCreatedAtDesc();

    long countByCreatedAtAfter(LocalDateTime after);
}
