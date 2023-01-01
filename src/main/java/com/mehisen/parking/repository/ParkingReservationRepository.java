package com.mehisen.parking.repository;

import com.mehisen.parking.entity.ParkingReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingReservationRepository extends JpaRepository<ParkingReservationEntity, Long> {
    Optional<ParkingReservationEntity> findFirstByUserOrSlot(UserEntity userEntity,SlotEntity slotEntity);

    Optional<ParkingReservationEntity> findFirstByUserAndSlot(UserEntity userEntity,SlotEntity slotEntity);

    Optional<ParkingReservationEntity> findFirstByUser(UserEntity userEntity);
}
