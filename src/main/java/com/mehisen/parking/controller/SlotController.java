package com.mehisen.parking.controller;

import com.mehisen.parking.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slot")
@RequiredArgsConstructor
public class SlotController {

    @Autowired
    private SlotService slotService;

    @PostMapping("/createNewSlot")
    public ResponseEntity<?> createNewSlot() {
        return slotService.createNewSlot();
    }

    @GetMapping("/allSlots")
    public ResponseEntity<?> allSlots() {
        return slotService.allSlots();
    }

}
