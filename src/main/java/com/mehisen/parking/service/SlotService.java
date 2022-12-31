package com.mehisen.parking.service;

import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.payload.resposne.SlotResponse;
import com.mehisen.parking.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;

    public ResponseEntity<?> createNewSlot() {
        try {
            SlotEntity slotEntity = slotRepository.save(new SlotEntity());

            return ResponseEntity.ok(slotFromEntity(slotEntity));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when creating new slot");
        }
    }

    public ResponseEntity<?> allSlots() {
        try {
            List<SlotEntity> allSlots = slotRepository.findAll();
            List<SlotResponse> result = allSlots.stream().map(this::slotFromEntity).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
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
