package com.mehisen.parking.repository;

import com.mehisen.parking.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryEntity,Long> {
}
