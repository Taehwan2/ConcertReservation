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
//콘서트 엔티티
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;
    //콘서트 이름
    private String name;
    //콘서트 장르
    private String Genre;
}
