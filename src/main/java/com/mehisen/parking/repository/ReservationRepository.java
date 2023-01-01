package com.mehisen.parking.repository;

import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Optional<ReservationEntity> findFirstByUserOrSlot(UserEntity userEntity, SlotEntity slotEntity);

    Optional<ReservationEntity> findFirstByUserAndSlot(UserEntity userEntity, SlotEntity slotEntity);

    Optional<ReservationEntity> findFirstByUser(UserEntity userEntity);
}
