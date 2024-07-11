package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("간단한 UserService getUserPoint 테스트")
    void getUserPointTest() {
        var user = new User(1L,"태환",new BigDecimal(10000));

        //given
        given(userRepository.getUserPoint(1L)).willReturn(user);

        //when
        var resultUser = userService.getUserPoint(1L);

        //then
        assertThat(resultUser.getName()).isEqualTo("태환");
        assertThat(resultUser.getName()).isEqualTo(user.getName());
        assertThat(resultUser.getPoint()).isEqualTo(user.getPoint());

    }
}