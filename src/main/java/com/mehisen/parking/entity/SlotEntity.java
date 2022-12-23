package com.mehisen.parking.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "slots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
