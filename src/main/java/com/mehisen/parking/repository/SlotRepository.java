package com.mehisen.parking.repository;

import com.mehisen.parking.entity.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<SlotEntity,Long> {
    Optional<SlotEntity> findFirstByUserId(Long userId);
}
