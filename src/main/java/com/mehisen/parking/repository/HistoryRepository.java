package com.mehisen.parking.repository;

import com.mehisen.parking.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity,Long> {
    List<HistoryEntity> findAllByCreationDateTimeAfter(Date today);
}
