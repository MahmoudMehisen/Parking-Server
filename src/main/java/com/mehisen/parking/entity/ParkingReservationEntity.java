package com.mehisen.parking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "parking_reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "slot_id",referencedColumnName = "id")
    private SlotEntity slot;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

}
