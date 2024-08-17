package com.example.concert.infrastructure.kafka.Test;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTestProducer {

    private final String TOPIC = "TEST_TOPIC";

    private final KafkaTemplate<String,String> stringKafkaTemplate;

    public void sendMessage(String message){
        log.info("Producer Message:{}",message);
        stringKafkaTemplate.send(TOPIC,message);
    }
}
