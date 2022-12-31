package com.mehisen.parking.payload.resposne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> roles;
}
