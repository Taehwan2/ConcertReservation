package com.example.concert.infrastructure.kafka.reservation;

import com.example.concert.domain.outBox.ReservationMessageProducer;
import com.example.concert.infrastructure.outbox.ReservationOutBox;
import com.example.concert.domain.outBox.ReservationOutBoxWriter;
import com.example.concert.infrastructure.outbox.ReservationOutBoxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationMessageProducerImpl implements ReservationMessageProducer {
    private static final String TOPIC = "exam-topic";

    private final KafkaTemplate<String, ReservationOutBox> kafkaTemplate;
    private final ReservationOutBoxWriter outBoxChange;
    public void sendMessage(ReservationOutBox box) {
        log.info("Produce message : {}", box);
        this.kafkaTemplate.send(TOPIC, box);

    }


    @Scheduled(fixedRate = 5 * 1000)
    void resendFailedMessage(){
        List<ReservationOutBox> outBox = outBoxChange.findOutBoxFailed(ReservationOutBoxStatus.INIT);
        if(outBox.isEmpty())return;
        outBox.forEach(
                i -> kafkaTemplate.send(TOPIC,i)
        );
    }

}
