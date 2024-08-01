package com.example.concert.domain.queue.service;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@EnableCaching
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RedisQueueServiceTest {
    private RedisServer redisServer;

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;

    @Autowired
    private RedisTemplate<String, String> customStringRedisTemplate;

    @Autowired
    private RedisQueueService redisQueueService;



    @Test
    @DisplayName("1L을 가진 유저를 waiting_queue에 넣고 순서를 가져왔을 떄 0번을 테스트 하는 것")
    void registerQueue() throws Exception {
        Long userId = 1L;
        QueueRequest queueRequest = new QueueRequest(1L);

        boolean result = redisQueueService.registerQueue(queueRequest);
        Long cachedRank = longRedisTemplate.opsForZSet().rank("waiting_queue", userId);

        assertThat(cachedRank).isEqualTo(0);
        assertThat(result).isTrue();

    }

    @Test
    @DisplayName("유저 아이디 1L인 사람의 대기열 순번을 가져오는 테스트 ")
    void getQueue() throws Exception {
        Long rank =  redisQueueService.getQueue(1L);
        assertThat(rank).isEqualTo(0);
    }

    @Test
    @DisplayName("1L이 active_token에 있는지 테스트")
    void findExpiredAtAndUpdate() {
       boolean result  = redisQueueService.findExpiredAtAndUpdate(1L);
       assertThat(result).isTrue();
    }

    @Test
    @DisplayName("현재 wating_queue에 있는 대기열들을 active_tokens로 옮겨주는 테스트 ")
    void checkExpiredAtAndUpdateDone() {
        redisQueueService.checkExpiredAtAndUpdateDone();
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens");

        assertThat(activeTokens.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("만료된 active_tokens들을 제거하는 테스트")
    void expiredAtAndUpdateDone() {
        redisQueueService.ExpiredAtAndUpdateDone();
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens");
        assertThat(activeTokens.size()).isEqualTo(0);
    }
}