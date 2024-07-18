package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
//좌석 유닛테스트
@ExtendWith(MockitoExtension.class)
class UserSeatFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private SeatService seatService;

    @InjectMocks
    private UserSeatFacade userSeatFacade;

    @Test
    @DisplayName("예약가능한 좌석번호를 1,2인 목객체를 반환하는 테스트 작성")
    void getAbleSeatsTest() throws Exception {
        ConcertSeat concertSeat = ConcertSeat.builder().concertSeatId(1L).build();
        ConcertSeat concertSeat2 = ConcertSeat.builder().concertSeatId(2L).build();
        var list = List.of(concertSeat,concertSeat2);

        //given.
        given(seatService.FindAbleSeats(1L)).willReturn(list);

        //when.
        var result = userSeatFacade.getAbleSeats(1L);

        //then.
        assertThat(result.get(0).getConcertSeatId()).isEqualTo(1L);
        assertThat(result.get(1).getConcertSeatId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("유저 아이디 1을 가진 사람이 1인 좌석을 예약하는 테스트")
    void reserveSeatsTest() throws Exception {
        var user = User.builder().userId(1L).build();
        ConcertSeat concertSeat = ConcertSeat.builder().concertSeatId(1L).seatNo(1).build();

        //given.
        given(userService.getUserPoint(1L)).willReturn(user);
        given(seatService.reserveSeatTemp(any(ConcertSeatRequest.class))).willReturn(concertSeat);

        //when.
        var result = userSeatFacade.reserveSeats(new ConcertSeatRequest(1L,1L,1));

        //then.
        assertThat(result.getSeatNo()).isEqualTo(concertSeat.getSeatNo());

    }
}