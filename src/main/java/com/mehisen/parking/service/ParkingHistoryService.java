package com.mehisen.parking.service;

import com.mehisen.parking.entity.ParkingHistoryEntity;
import com.mehisen.parking.entity.ParkingReservationEntity;
import com.mehisen.parking.repository.ParkingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingHistoryService {

    final private ParkingHistoryRepository parkingHistoryRepository;

    public void addParkingHistory(ParkingReservationEntity parkingReservationEntity) {

        ParkingHistoryEntity parkingHistoryEntity = new ParkingHistoryEntity();
        parkingHistoryEntity.setSlot(parkingReservationEntity.getSlot());
        parkingHistoryEntity.setUser(parkingReservationEntity.getUser());
        parkingHistoryEntity.setCreationDateTime(parkingReservationEntity.getCreationDateTime());
        parkingHistoryEntity.setLeaveDateTime(new Date());

        parkingHistoryRepository.save(parkingHistoryEntity);
    }
}
