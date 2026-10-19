package com.example.demo.utils.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
}