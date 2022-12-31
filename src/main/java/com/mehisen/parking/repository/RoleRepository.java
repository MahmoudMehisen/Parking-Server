package com.mehisen.parking.repository;

import com.mehisen.parking.entity.ERole;
import com.mehisen.parking.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
}
