package com.mehisen.parking.controller;

import com.mehisen.parking.entity.HistoryEntity;
import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.payload.resposne.HistoryResponse;
import com.mehisen.parking.payload.resposne.ReservationResponse;
import com.mehisen.parking.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/history")
@Slf4j
@RequiredArgsConstructor
public class HistoryController {

    final private HistoryService historyService;

    @GetMapping("/getTodayReservation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTodayReservation() {
        try {
            List<HistoryEntity> historyEntities = historyService.getTodayReservation();
            List<HistoryResponse> result = historyEntities.stream().map(this::getHistoryResponse).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when get current reservation");
        }
    }

    private HistoryResponse getHistoryResponse(HistoryEntity historyEntity) {
        return new HistoryResponse(historyEntity.getId(), historyEntity.getUser().getId(), historyEntity.getSlot().getId(), historyEntity.getCreationDateTime(), historyEntity.getLeaveDateTime());
    }
}
