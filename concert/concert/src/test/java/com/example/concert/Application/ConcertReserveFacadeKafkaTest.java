package com.example.concert.Application;

import com.example.concert.Presentation.api.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.consumer.Test.KafkaTestConsumer;
import com.example.concert.Presentation.consumer.reservation.ReservationMessageConsumer;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concert.service.ConcertService;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailRepository;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import com.example.concert.domain.reservation.service.ReservationRepository;
import com.example.concert.domain.reservation.service.ReservationService;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import com.example.concert.infrastructure.outbox.ReservationOutBoxImpl;
import com.example.concert.infrastructure.outbox.ReservationOutBoxRepository;
import com.example.concert.infrastructure.outbox.ReservationOutBoxStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ConcertReserveFacadeKafkaTest {

    @Autowired
    private  UserService userService;

    @Autowired
    private ConcertReserveFacade concertReserveFacade;

    @Autowired
    private ConcertDetailService concertDetailService;

    @Autowired
    private  SeatService seatService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ReservationMessageConsumer consumer;

    @Autowired
    private ReservationOutBoxImpl reservationOutBox;

    @BeforeEach
    void setUp() throws Exception {

        userService.save(User.builder().point(new BigDecimal(10000)).name("taehwan7").build());
        concertService.saveConcert(Concert.builder().Genre("action").name("Avengerse").build());
        concertDetailService.saveConcertDetail(ConcertDetail.builder().concertId(1L).reservationStartDate(LocalDateTime.now().plusMinutes(10)).startDate(LocalDateTime.now().plusMinutes(10)).build());
        seatService.reserveSeatTemp(ConcertSeatRequest.builder().userId(1L).concertDetailId(1L).seatNo(2).build());


    }

    @Test
    @DisplayName("예약 여부가 true가 나오는 테스트 -> 커밋이 완료된다면 outBox가 생성되고, kafka 메세지가 발행되면서 status 가 Published 로 바뀐다.")
    /*TODO
           대기얄 기능을 위해서 redis를 킨상태에서 userId가 1인 사람만 active상태로 만든 후 테스트
     */
    //2024-08-15 10:09:54.483357,1,2024-08-15 10:09:54.483357,"{""createdAt"":[2024,8,15,19,9,54,461922000],""updatedAt"":[2024,8,15,19,9,54,461922000],""reservationId"":1,""userId"":1,""seatId"":1,""concertId"":1,""concertDetailId"":1}",INIT
    //하지만 INIT인거 보니깐 Kafka문제가 확실하니 Published로 바뀌도록 조치를 취해야함
    void getSuccessPayment() throws Exception {
        var result = concertReserveFacade.concertPayment(ConcertSeatRequest.builder().userId(1L).concertDetailId(1L).seatNo(2).build());
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertThat(result.isCheck()).isEqualTo(true);

        assertThat(reservationOutBox.findById(1L).getOutBoxStatus()).isEqualTo(ReservationOutBoxStatus.PUBLISHED);

    }
    @Test
    @DisplayName("예약 여부가 true가 나오는 테스트 -> 커밋이 완료된다면 outBox가 생성되고, getLatch를 하지않으면 Kafka에서 바로 메세지를 보내는 속도를 놓쳐 PUBLISHED가 되지않고, OUTBOX가 INIT상태로 저장만 된다. ")

    void getSuccessPayment2() throws Exception {
        var result = concertReserveFacade.concertPayment(ConcertSeatRequest.builder().userId(1L).concertDetailId(1L).seatNo(2).build());

        //TOdo 정확하게         boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS); 이 부분을 안쓰면 kafka가 메세지를 안보내는 이유를 모르겠어요 ㅜㅠㅜㅠㅠㅠㅠ
        assertThat(reservationOutBox.findById(1L).getOutBoxStatus()).isEqualTo(ReservationOutBoxStatus.INIT);

    }


    @Test
    @DisplayName("예약 여부가 true가 나오는 테스트 일부로 메인 로직에 Exception을 터트리기-> 커밋이 실패된다면 reservation OUtBOX가 롤벡되는지 확인 실제 코드를 바꿔야함. ")
    void getSuccessPayment3() throws Exception {
        var result = concertReserveFacade.concertPayment(ConcertSeatRequest.builder().userId(1L).concertDetailId(1L).seatNo(2).build());
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertThatThrownBy(() -> reservationOutBox.findById(1L))
                .isInstanceOf(BusinessBaseException.class)
                .hasMessageContaining(ErrorCode.OUTBOX_NOT_FOUND.name());

    }



}