package com.mehisen.parking.service;

import com.mehisen.parking.entity.ParkingReservationEntity;
import com.mehisen.parking.payload.request.ParkingReservationRequest;
import com.mehisen.parking.repository.ParkingReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingReservationService {
    final private ParkingReservationRepository parkingReservationRepository;

    public ResponseEntity<?> checkin(ParkingReservationRequest parkingReservationRequest){
        try {
            Optional<ParkingReservationEntity> parkingReservation = parkingReservationRepository.findByUserId(parkingReservationRequest.getUserId());
            if(parkingReservation.isPresent()){
                return ResponseEntity.status(400).body("Error user already checkin");
            }

             parkingReservation = parkingReservationRepository.findBySlotId(parkingReservationRequest.getSlotId());
            if(parkingReservation.isPresent()){
                return ResponseEntity.status(400).body("Error slot already checked-in");
            }



            // parkingReservationRepository.save(new ParkingReservationEntity(parkingReservationRequest.getUserId(), parkingReservationRequest.getSlotId(),new Date()));
            return ResponseEntity.status(400).body("Error when checkin");
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when checkin");
        }
    }
}
