package com.example.concert.domain.concert.entity;

import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;

    private String name;

    private String Genre;
}
