package com.mehisen.parking.controller;

import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.payload.resposne.SlotResponse;
import com.mehisen.parking.service.SlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/slot")
@Slf4j
public class SlotController {

    final private SlotService slotService;

    @PostMapping("/createNewSlot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewSlot() {
        try {
            SlotEntity slotEntity = slotService.createNewSlot();

            return ResponseEntity.ok(slotFromEntity(slotEntity));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when creating new slot");
        }
    }

    @GetMapping("/allSlots")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> allSlots() {
        try {
            List<SlotEntity> allSlots = slotService.allSlots();
            List<SlotResponse> result = allSlots.stream().map(this::slotFromEntity).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when fetch all slots");
        }
    }

    private SlotResponse slotFromEntity(SlotEntity slotEntity) {
        SlotResponse slotResponse = new SlotResponse(slotEntity.getId(), 0L);
        if (slotEntity.getUser() != null) {
            slotResponse.setUserId(slotEntity.getUser().getId());
        }
        return slotResponse;
    }
}
