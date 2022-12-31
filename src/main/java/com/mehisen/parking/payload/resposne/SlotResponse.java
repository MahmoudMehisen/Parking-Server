package com.mehisen.parking.payload.resposne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotResponse {
    private Long id;
    private Long userId;
}
