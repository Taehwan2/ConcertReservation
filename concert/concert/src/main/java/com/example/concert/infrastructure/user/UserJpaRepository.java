package com.example.concert.infrastructure.user;

import com.example.concert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//실제 구현체
public interface UserJpaRepository extends JpaRepository<User,Long> {

}
