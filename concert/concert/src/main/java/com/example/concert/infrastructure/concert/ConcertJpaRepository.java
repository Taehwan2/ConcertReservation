package com.example.concert.infrastructure.concert;

import com.example.concert.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
//실제 jpa 구현체
public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {
}
