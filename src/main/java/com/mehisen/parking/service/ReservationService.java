package com.mehisen.parking.service;

import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


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

    public void checkout(ReservationEntity reservationEntity) {
        reservationRepository.delete(reservationEntity);
    }


    public ReservationEntity checkIfFoundAnyReservationForUserOrSlot(UserEntity userEntity, SlotEntity slotEntity){
        return reservationRepository.findFirstByUserOrSlot(userEntity,slotEntity).get();
    }
    public ReservationEntity checkIfFoundAnyReservationForUserAndSlot(UserEntity userEntity, SlotEntity slotEntity){
        return reservationRepository.findFirstByUserAndSlot(userEntity,slotEntity).get();
    }




    public ReservationEntity checkIfFoundReservationByUser(UserEntity userEntity){
        return reservationRepository.findFirstByUser(userEntity).get();
    }


}
