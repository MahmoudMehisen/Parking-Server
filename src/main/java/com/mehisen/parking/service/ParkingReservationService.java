package com.mehisen.parking.service;

import com.mehisen.parking.entity.ParkingHistoryEntity;
import com.mehisen.parking.entity.ParkingReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.repository.ParkingHistoryRepository;
import com.mehisen.parking.repository.ParkingReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingReservationService {
    final private ParkingReservationRepository parkingReservationRepository;

    public ParkingReservationEntity checkin(UserEntity userEntity, SlotEntity slotEntity) {
        ParkingReservationEntity parkingReservationEntity = new ParkingReservationEntity();
        parkingReservationEntity.setSlot(slotEntity);
        parkingReservationEntity.setUser(userEntity);
        parkingReservationEntity.setCreationDateTime(new Date());
        return parkingReservationRepository.save(parkingReservationEntity);
    }

    public void checkout(ParkingReservationEntity parkingReservationEntity) {
        parkingReservationRepository.delete(parkingReservationEntity);
    }


    public ParkingReservationEntity checkIfFoundAnyReservationForUserOrSlot(UserEntity userEntity, SlotEntity slotEntity){
        return parkingReservationRepository.findFirstByUserOrSlot(userEntity,slotEntity).get();
    }
    public ParkingReservationEntity checkIfFoundAnyReservationForUserAndSlot(UserEntity userEntity, SlotEntity slotEntity){
        return parkingReservationRepository.findFirstByUserAndSlot(userEntity,slotEntity).get();
    }




    public ParkingReservationEntity checkIfFoundReservationByUser(UserEntity userEntity){
        return parkingReservationRepository.findFirstByUser(userEntity).get();
    }


}
