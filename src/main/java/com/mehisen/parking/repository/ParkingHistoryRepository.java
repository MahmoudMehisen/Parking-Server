package com.mehisen.parking.repository;

import com.mehisen.parking.entity.ParkingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingHistoryRepository extends JpaRepository<ParkingHistoryEntity,Long> {
}
