package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
//좌석 유닛 테스트
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    @DisplayName("예약가능 한 좌석 목 객체로 만들고 검증하기.")
    void testFindAbleSeats() throws Exception {
        given(seatRepository.findStatusReserved(any(Long.class), any(SeatStatus.class)))
                .willReturn(Collections.emptyList()); //given

        List<ConcertSeat> availableSeats = seatService.FindAbleSeats(1L); //when
        //then
        assertNotNull(availableSeats);
        assertEquals(10, availableSeats.size());
    }

    @Test
    @DisplayName("예약하기 테스트.")
    void testReserveSeatTemp() throws Exception {
        ConcertSeatRequest request = new ConcertSeatRequest(1L,1L,1);
        //given
        given(seatRepository.findSeat(any(Long.class), any(int.class))).willReturn(null);
        given(seatRepository.createSeat(any(ConcertSeat.class))).willAnswer(invocation -> invocation.getArgument(0));
        //when
        ConcertSeat reservedSeat = seatService.reserveSeatTemp(request);
        //then
        assertNotNull(reservedSeat);
        assertEquals(SeatStatus.TEMP, reservedSeat.getSeatStatus());
        assertEquals(1L, reservedSeat.getUserId());
        assertEquals(1, reservedSeat.getSeatNo());
        assertEquals(new BigDecimal(1000), reservedSeat.getPrice());
    }

    @Test
    @DisplayName("user가  예약했던 좌석들을 모두 불러오는 것.")
    void testReserveSeatTemp_AlreadyReserved() throws Exception {
        ConcertSeatRequest request = new ConcertSeatRequest(1L,1L,1);

        //given
        ConcertSeat existingSeat = ConcertSeat.builder()
                .concertDetailId(1L)
                .userId(2L)
                .seatNo(1)
                .seatStatus(SeatStatus.RESERVABLE)
                .price(new BigDecimal(1000))
                .build();

        given(seatRepository.findSeat(any(Long.class), any(int.class))).willReturn(existingSeat);
        given(seatRepository.createSeat(any(ConcertSeat.class))).willAnswer(invocation -> invocation.getArgument(0));
        //when
        ConcertSeat reservedSeat = seatService.reserveSeatTemp(request);
        //then
        assertNotNull(reservedSeat);
        assertEquals(SeatStatus.TEMP, reservedSeat.getSeatStatus());
        assertEquals(1L, reservedSeat.getUserId());
        assertEquals(1, reservedSeat.getSeatNo());
    }

    @Test
    @DisplayName("예약이 만료된 좌석 1,2를 찾아서 가져오는 테스트")
    void testCheckExpiredSeat() {
        List<ConcertSeat> expiredSeats = List.of(
                ConcertSeat.builder().concertSeatId(1L).build(),
                ConcertSeat.builder().concertSeatId(2L).build()
        );
        //given
        given(seatRepository.findExpiredInTemp(any(SeatStatus.class), any(LocalDateTime.class))).willReturn(expiredSeats);
        //when
        seatService.checkExpiredSeat();
         //then
        verify(seatRepository, times(1)).changeSeatsStatus(List.of(1L, 2L));
    }

    @Test
    @DisplayName("user가  예약했던 좌석들을 모두 불러오는 것.")
    void findTempSeatTest(){
        List<ConcertSeat> expiredSeats = List.of(
                ConcertSeat.builder().concertSeatId(1L).seatStatus(SeatStatus.TEMP).build(),
                ConcertSeat.builder().concertSeatId(2L).seatStatus(SeatStatus.TEMP).build()
        );
        //given.
        given(seatRepository.findTempSeatByUserId(1L)).willReturn(expiredSeats);

        //when.
        var result = seatService.findTempSeatByUserId(1L);

        //then.
        assertThat(result.get(0).getSeatStatus()).isEqualTo(expiredSeats.get(0).getSeatStatus());
    }

}