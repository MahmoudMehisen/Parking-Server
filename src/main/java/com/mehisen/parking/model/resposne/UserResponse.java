package com.mehisen.parking.model.resposne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private String email;
    private long slotId;
    private Boolean isVip;
    private Boolean isComing;
}
