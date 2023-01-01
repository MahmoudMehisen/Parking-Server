package com.mehisen.parking.controller;

import com.mehisen.parking.entity.ParkingReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.ParkingReservationRequest;
import com.mehisen.parking.service.ParkingHistoryService;
import com.mehisen.parking.service.ParkingReservationService;
import com.mehisen.parking.service.SlotService;
import com.mehisen.parking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/ParkingReservation")
@Slf4j
public class ParkingReservationController {

    final private ParkingReservationService parkingReservationService;

    final private SlotService slotService;

    final private UserService userService;

    final private ParkingHistoryService parkingHistoryService;

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@Valid @RequestBody ParkingReservationRequest parkingReservationRequest) {
        try {
            UserEntity userEntity = userService.userInfo(parkingReservationRequest.getUserId());

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }

            SlotEntity slotEntity = slotService.findById(parkingReservationRequest.getSlotId());

            if (slotEntity == null) {
                return ResponseEntity.status(400).body("Error slot not found");
            }

            ParkingReservationEntity parkingReservationEntity = parkingReservationService.checkIfFoundAnyReservationForUserOrSlot(userEntity, slotEntity);
            if (parkingReservationEntity != null) {
                if (Objects.equals(parkingReservationEntity.getUser().getId(), userEntity.getId()))
                    return ResponseEntity.status(400).body("Error user already checked-in");
                else
                    return ResponseEntity.status(400).body("Error slot already checked-in");
            }

            ParkingReservationEntity newParkingReservationEntity = parkingReservationService.checkin(userEntity, slotEntity);
            return ResponseEntity.ok(newParkingReservationEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody ParkingReservationRequest parkingReservationRequest) {
        try {
            UserEntity userEntity = userService.userInfo(parkingReservationRequest.getUserId());

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }

            SlotEntity slotEntity = slotService.findById(parkingReservationRequest.getSlotId());

            if (slotEntity == null) {
                return ResponseEntity.status(400).body("Error slot not found");
            }

            ParkingReservationEntity parkingReservationEntity = parkingReservationService.checkIfFoundAnyReservationForUserAndSlot(userEntity, slotEntity);

            if (parkingReservationEntity == null) {
                return ResponseEntity.status(400).body("Error no reservation found");
            }

            parkingHistoryService.addParkingHistory(parkingReservationEntity);
            parkingReservationService.checkout(parkingReservationEntity);

            return ResponseEntity.ok("Checkout successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }

    @GetMapping("/isUserHasSlot/{id}")
    public ResponseEntity<?> isUserHasSlot(@PathVariable Long id) {
        try {
            UserEntity userEntity = userService.userInfo(id);

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }

            ParkingReservationEntity parkingReservationEntity = parkingReservationService.checkIfFoundReservationByUser(userEntity);

            return ResponseEntity.ok(parkingReservationEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when check is user has slot");
        }
    }

}
