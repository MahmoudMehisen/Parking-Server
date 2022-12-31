package com.mehisen.parking.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingReservationRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private Long slotId;
}
