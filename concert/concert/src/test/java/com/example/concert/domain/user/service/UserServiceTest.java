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
    //userService 를 인젝트 목으로 두고 서비스 테스트
    @Mock
    private UserRepository userRepository;
    //레파지토리는 목으로
    @Test
    @DisplayName("간단한 UserService getUserPoint 테스트.")
    void getUserPointTest() {
        var user = new User(1L,"태환",new BigDecimal(10000));

        //given
        given(userRepository.getUserPoint(1L)).willReturn(user);  //객체 입력

        //when
        var resultUser = userService.getUserPoint(1L);   // 실제 서비스 호출

        //then
        assertThat(resultUser.getName()).isEqualTo("태환"); //검증
        assertThat(resultUser.getName()).isEqualTo(user.getName());  // 검증
        assertThat(resultUser.getPoint()).isEqualTo(user.getPoint()); //검증

    }
}