package com.mehisen.parking.controller;

import com.mehisen.parking.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/slot")
public class SlotController {

    @Autowired
    private SlotService slotService;

    @PostMapping("/createNewSlot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewSlot() {
        return slotService.createNewSlot();
    }

    @GetMapping("/allSlots")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> allSlots() {
        return slotService.allSlots();
    }

}
