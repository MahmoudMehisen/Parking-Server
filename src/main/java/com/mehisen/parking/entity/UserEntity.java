package com.mehisen.parking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;



@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "slot_id",referencedColumnName = "id")
    private SlotEntity Slot;
    private Boolean isVip = false;
    private Boolean isComing = true;
}
