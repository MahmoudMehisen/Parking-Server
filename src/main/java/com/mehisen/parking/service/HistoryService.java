package com.mehisen.parking.service;

import com.mehisen.parking.entity.HistoryEntity;
import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    final private HistoryRepository historyRepository;

    public void addParkingHistory(ReservationEntity reservationEntity) {

        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setSlot(reservationEntity.getSlot());
        historyEntity.setUser(reservationEntity.getUser());
        historyEntity.setCreationDateTime(reservationEntity.getCreationDateTime());
        historyEntity.setLeaveDateTime(new Date());

        historyRepository.save(historyEntity);
    }
}
