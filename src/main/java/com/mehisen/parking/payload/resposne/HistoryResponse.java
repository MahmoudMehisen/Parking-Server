package com.mehisen.parking.payload.resposne;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private Long id;
    private Long userId;
    private Long slotId;
    private Date creationDateTime;
    private Date leaveDateTime;
}
