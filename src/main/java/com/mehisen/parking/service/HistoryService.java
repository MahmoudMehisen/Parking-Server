package com.mehisen.parking.service;

import com.mehisen.parking.entity.HistoryEntity;
import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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

    public List<HistoryEntity> getTodayReservation() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        return historyRepository.findAllByCreationDateTimeAfter(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
