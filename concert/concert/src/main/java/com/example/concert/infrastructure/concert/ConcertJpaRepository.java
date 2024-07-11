package com.example.concert.infrastructure.concert;

import com.example.concert.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {
}
