package com.mehisen.parking.model.resposne;

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
