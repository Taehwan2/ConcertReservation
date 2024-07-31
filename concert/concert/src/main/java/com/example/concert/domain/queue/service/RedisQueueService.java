package com.example.concert.domain.queue.service;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisQueueService {

    private final RedisTemplate<String, Long> longRedisTemplate;
    private final RedisTemplate<String, String> customStringRedisTemplate;

    public Long getQueue(Long waitId) throws Exception {
        Long rank =  longRedisTemplate.opsForZSet().rank("waiting_queue",waitId);
        if(rank == null) {
            return -1L;
        }
        return rank;
    }

    @Transactional
    public void registerQueue(QueueRequest queueRequest) throws Exception {
        double score = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        Long userId = queueRequest.getUserId(); //유저를 가져와서
        longRedisTemplate.opsForZSet().add("waiting_queue",userId, score);

    }

/*
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void  checkExpiredAtAndUpdateDone(){
        Set<Long> waitingTokens = longRedisTemplate.opsForZSet().range("waiting_queue", 0, 5);
        System.out.println(waitingTokens);
        if (waitingTokens == null || waitingTokens.isEmpty()) {
            return;
        }
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        for(Long userId: waitingTokens){
            String token = "token:"+userId;
            String metaInfo = userId + "," + (currentTime + 180);
            customStringRedisTemplate.opsForSet().add("active_tokens", token + ":" + metaInfo);
            longRedisTemplate.opsForZSet().remove("waiting_queue", userId);
        }


    }

 */
    public  boolean checkActive(){

        return  true;
    }
/*
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void  ExpiredAtAndUpdateDone(){
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens");
        if (activeTokens == null || activeTokens.isEmpty()) {
            return;
        }
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Set<String> expiredTokens = activeTokens.stream()
                .filter(token -> {
                    String[] parts = token.split(":")[2].split(",");
                    long expirationTime = Long.parseLong(parts[1]);
                    return expirationTime < currentTime;
                })
                .collect(Collectors.toSet());
    }

*/

}
