package com.example.concert.Presentation.config;

import com.example.concert.infrastructure.outbox.ReservationOutBox;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration

@EnableKafka
public class KafkaConsumerConfig {


    private String bootstrapServers = "localhost:9092";

       @Bean
       public ConsumerFactory<String, ReservationOutBox> consumerFactory() {

           Map<String, Object> config = new HashMap<>();
           config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
           config.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");

           return new DefaultKafkaConsumerFactory<>(
                   config,
                   new StringDeserializer(),
                   new JsonDeserializer<>(ReservationOutBox.class));
       }

       @Bean
       public ConcurrentKafkaListenerContainerFactory<String, ReservationOutBox> kafkaListener() {
           ConcurrentKafkaListenerContainerFactory<String, ReservationOutBox> factory = new ConcurrentKafkaListenerContainerFactory<>();
           factory.setConsumerFactory(consumerFactory());
           return factory;
       }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");  // KafkaTestConsumer의 그룹 ID와 일치시킴
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> factory2() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }



}
