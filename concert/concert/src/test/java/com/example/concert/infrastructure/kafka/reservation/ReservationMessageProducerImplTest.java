package com.example.concert.infrastructure.kafka.reservation;


import com.example.concert.Presentation.consumer.reservation.ReservationMessageConsumer;
import com.example.concert.domain.outBox.ReservationOutBoxWriter;
import com.example.concert.infrastructure.outbox.ReservationOutBox;
import com.example.concert.infrastructure.outbox.ReservationOutBoxStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ReservationMessageProducerImplTest {

    @Autowired
    private ReservationMessageProducerImpl reservationMessageProducer;


    @Autowired
    private ReservationMessageConsumer consumer;

    @MockBean
    private ReservationOutBoxWriter outBoxWriter;

    @MockBean
    private KafkaTemplate<String, ReservationOutBox> kafkaTemplate;

    @MockBean
    private TaskScheduler taskScheduler;  // 스케줄러를 모킹하여 실제 실행되지 않도록 함
    @Test
    @DisplayName("Failed 메시지를 재전송하는 스케줄러 테스트 두개의 실패된 메세지가 kafkaTemplate으로 재실행된다.")
    void testResendFailedMessage() throws InterruptedException {
        // Arrange
        List<ReservationOutBox> failedOutBoxes = List.of(
                new ReservationOutBox(1L, "message1", ReservationOutBoxStatus.INIT),
                new ReservationOutBox(2L, "message2", ReservationOutBoxStatus.INIT)
        );

        // Mock the behavior
        given(outBoxWriter.findOutBoxFailed(ReservationOutBoxStatus.INIT)).willReturn(failedOutBoxes);

        // Act
        reservationMessageProducer.resendFailedMessage();  // 스케줄러 메서드를 수동으로 호출

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        // Assert
        verify(kafkaTemplate, times(2)).send(anyString(), any(ReservationOutBox.class));
        verify(outBoxWriter, times(1)).findOutBoxFailed(ReservationOutBoxStatus.INIT);
    }

}