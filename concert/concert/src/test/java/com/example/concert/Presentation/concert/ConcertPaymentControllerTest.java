package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertReserveFacade;
import com.example.concert.Presentation.concert.model.reservation.ReservationResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.reservation.entity.Reservation;
import com.example.concert.Presentation.concert.mapper.ReservationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureMockMvc
class ConcertPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcertReserveFacade concertReserveFacade;

    @MockBean
    private ReservationMapper reservationMapper;

    @Test
    @DisplayName("결제시스템 로직 검증")
    void paymentTest() throws Exception {
        Reservation reservation = new Reservation(
                1L,
                1L,
                1L,
                1L,
                1L
        );

        ReservationResponse reservationResponse = ReservationResponse.builder()
                .concertName("Concert Name")
                .concertGenre("Rock")
                .startDate(LocalDateTime.of(2023, 8, 15, 20, 0))
                .seatNo("A1")
                .userName("John Doe")
                .price("100.00")
                .build();

        //Todo 예약완료후 다른 로직들과 Facade계층을 통해서 ReservationResponse를 반환하는 로직
        given(concertReserveFacade.concertPayment(any(ConcertSeatRequest.class))).willReturn(any());
        given(reservationMapper.entityToResponse(any(Reservation.class))).willReturn(reservationResponse);

        String requestContent = "{\"userId\": 1, \"seatId\": 1}";

        mockMvc.perform(post("/concert/reservation/days/1")
                        .contentType(APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertName").value("Concert Name"))
                .andExpect(jsonPath("$.concertGenre").value("Rock"))
                .andExpect(jsonPath("$.startDate").value("2023-08-15T20:00:00"))
                .andExpect(jsonPath("$.seatNo").value("A1"))
                .andExpect(jsonPath("$.userName").value("John Doe"))
                .andExpect(jsonPath("$.price").value("100.00"));
    }
}