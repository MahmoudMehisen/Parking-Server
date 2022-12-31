package com.mehisen.parking.controller;

import com.mehisen.parking.payload.request.ParkingReservationRequest;
import com.mehisen.parking.service.ParkingReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/ParkingReservation")
public class ParkingReservationController {

    final private ParkingReservationService parkingReservationService;

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@Valid @RequestBody ParkingReservationRequest parkingReservation){
        return parkingReservationService.checkin(parkingReservation);
    }
}
