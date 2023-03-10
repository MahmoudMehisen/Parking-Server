package com.mehisen.parking.controller;

import com.mehisen.parking.entity.ReservationEntity;
import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.ReservationRequest;
import com.mehisen.parking.payload.resposne.ReservationResponse;
import com.mehisen.parking.service.HistoryService;
import com.mehisen.parking.service.ReservationService;
import com.mehisen.parking.service.SlotService;
import com.mehisen.parking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/reservation")
@Slf4j
public class ReservationController {

    final private ReservationService reservationService;

    final private SlotService slotService;

    final private UserService userService;

    final private HistoryService historyService;

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> checkin(@Valid @RequestBody ReservationRequest reservationRequest) {
        try {
            UserEntity userEntity = userService.userInfo(reservationRequest.getUserId());

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }

            SlotEntity slotEntity = slotService.findById(reservationRequest.getSlotId());

            if (slotEntity == null) {
                return ResponseEntity.status(400).body("Error slot not found");
            }

            if (slotEntity.getUser() != null) {
                return ResponseEntity.status(400).body("Error slot for vip user only");
            }

            ReservationEntity reservationEntity = reservationService.checkIfFoundAnyReservationForUserOrSlot(userEntity, slotEntity);
            if (reservationEntity != null) {
                if (Objects.equals(reservationEntity.getUser().getId(), userEntity.getId()))
                    return ResponseEntity.status(400).body("Error user already checked-in");
                else
                    return ResponseEntity.status(400).body("Error slot already checked-in");
            }

            ReservationEntity newReservationEntity = reservationService.checkin(userEntity, slotEntity);
            return ResponseEntity.ok(getReservationResponse(newReservationEntity));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }

    @PostMapping("/checkout/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> checkout(@PathVariable Long id) {
        try {
            ReservationEntity reservationEntity = reservationService.findById(id);

            if (reservationEntity == null) {
                return ResponseEntity.status(400).body("Error no reservation found");
            }

            historyService.addParkingHistory(reservationEntity);
            reservationService.checkout(id);

            return ResponseEntity.ok("Checkout successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }

    @GetMapping("/isUserHasSlot/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> isUserHasSlot(@PathVariable Long id) {
        try {
            UserEntity userEntity = userService.userInfo(id);

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }

            ReservationEntity reservationEntity = reservationService.checkIfFoundReservationByUser(userEntity);
            if (reservationEntity == null) {
                return ResponseEntity.ok(false);
            }
            return ResponseEntity.ok(getReservationResponse(reservationEntity));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when check is user has reservation");
        }
    }

    @GetMapping("/getCurrentReservation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCurrentReservation() {
        try {
            List<ReservationEntity> reservations = reservationService.getCurrentReservation();
            List<ReservationResponse> result = reservations.stream().map(this::getReservationResponse).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when get current reservation");
        }
    }

    @PostMapping("/checkinVIP/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> checkinVIP(@PathVariable Long id) {
        try {
            UserEntity userEntity = userService.userInfo(id);

            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error user not found");
            }
            if (!userEntity.getIsVip() || userEntity.getSlot() == null) {
                return ResponseEntity.status(400).body("Error user not VIP");
            }

            ReservationEntity reservationEntity = reservationService.checkIfFoundAnyReservationForUserOrSlot(userEntity, userEntity.getSlot());

            if (reservationEntity != null) {
                if (Objects.equals(reservationEntity.getUser().getId(), userEntity.getId()))
                    return ResponseEntity.status(400).body("Error user already checked-in");
                else
                    return ResponseEntity.status(400).body("Error slot already checked-in");
            }

            ReservationEntity newReservationEntity = reservationService.checkin(userEntity, userEntity.getSlot());
            return ResponseEntity.ok(getReservationResponse(newReservationEntity));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }

    private ReservationResponse getReservationResponse(ReservationEntity reservationEntity) {
        return new ReservationResponse(reservationEntity.getId(), reservationEntity.getUser().getId(), reservationEntity.getSlot().getId(), reservationEntity.getCreationDateTime());
    }


}
