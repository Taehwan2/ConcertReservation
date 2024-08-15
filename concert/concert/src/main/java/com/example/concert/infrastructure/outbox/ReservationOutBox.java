package com.example.concert.infrastructure.outbox;

import com.example.concert.Presentation.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation_out_box")
public class ReservationOutBox extends BaseEntity {
    @Id
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private ReservationOutBoxStatus outBoxStatus;

}
