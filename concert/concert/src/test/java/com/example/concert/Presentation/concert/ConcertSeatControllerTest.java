package com.example.concert.Presentation.concert;

import com.example.concert.Application.UserSeatFacade;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.concert.Application.UserQueueFacade;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class ConcertSeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSeatFacade userSeatFacade;

    @Test
    @DisplayName("사용가능한 두자리를 조회할 수 있는 테스트")
    void getAbleSeatsTest() throws Exception {
        ConcertSeat seat1 = new ConcertSeat(
                1L,
                100L,
                SeatStatus.CANRESERVATION,
                "1",
                new BigDecimal("100.00")
        );

        ConcertSeat seat2 = new ConcertSeat(
                2L,
                100L,
                SeatStatus.CANRESERVATION,
                "2",
                new BigDecimal("120.00")
        );

        List<ConcertSeat> seats = List.of(seat1, seat2);

        //given
        given(userSeatFacade.getAbleSeats(1L)).willReturn(seats);


        //when & then
        //todo 결과는 위에 주어진 두 can 예약가능한 자리가 나야함
        mockMvc.perform(get("/concert/seats/"+1L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNo").value(1L))
                .andExpect(jsonPath("$[1].seatNo").value(2L));
    }

    @Test
    @DisplayName("좌석 예약을 검증하는 테스트")
    void enrollSeatsTest() throws Exception {
        ConcertSeat seat = new ConcertSeat(
                1L,
                100L,
                SeatStatus.CANRESERVATION,
                "1",
                new BigDecimal("100.00")
        );

        //given
        given(userSeatFacade.reserveSeats(any(ConcertSeatRequest.class))).willReturn(seat);

        String requestJson = "{\"concertSeatId\":1,\"concertDetailId\":1}";

        //when & then
        //Todo 좌석이 불가능하다고 바뀌어야함.하지만 로직이 없음으로 검증 아직 실패
        mockMvc.perform(patch("/concert/seat")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNo").value(1));
    }

}