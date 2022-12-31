package com.mehisen.parking.repository;

import com.mehisen.parking.entity.ParkingReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingReservationRepository extends JpaRepository<ParkingReservationEntity, Long> {
    Optional<ParkingReservationEntity> findByUserId(Long userid);
    Optional<ParkingReservationEntity> findBySlotId(Long slotId);
}
