package com.mehisen.parking.service;

import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    final private ReservationRepository reservationRepository;

    public ReservationEntity checkin(UserEntity userEntity, SlotEntity slotEntity) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setSlot(slotEntity);
        reservationEntity.setUser(userEntity);
        reservationEntity.setCreationDateTime(new Date());
        return reservationRepository.save(reservationEntity);
    }

    public void checkout(Long id) {
        reservationRepository.deleteById(id);
    }


    public ReservationEntity checkIfFoundAnyReservationForUserOrSlot(UserEntity userEntity, SlotEntity slotEntity) {
        return getReservationEntityValue(reservationRepository.findFirstByUserOrSlot(userEntity, slotEntity));
    }

    public ReservationEntity checkIfFoundReservationByUser(UserEntity userEntity) {
        return getReservationEntityValue(reservationRepository.findFirstByUser(userEntity));
    }

    public List<ReservationEntity> getCurrentReservation() {
        return reservationRepository.findAll();
    }


    public ReservationEntity findById(Long id) {
        return getReservationEntityValue(reservationRepository.findById(id));
    }

    private ReservationEntity getReservationEntityValue(Optional<ReservationEntity> reservationEntity) {
        return reservationEntity.isPresent() ? reservationEntity.get() : null;
    }
}
