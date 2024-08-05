package com.example.concert.domain.queue.service;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
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
    private final RedisTemplate<String, String>   customStringRedisTemplate;


    public Long getQueue(Long waitId) throws Exception {
        Long rank = longRedisTemplate.opsForZSet().rank("waiting_queue",waitId);  //wait 대기열에 uestid 를 통해 조회해서 순위를 얻어온다.
        log.info("대기번호: {}",rank);
        if(findExpiredAtAndUpdate(waitId))throw new BusinessBaseException(ErrorCode.QUEUE_ALREADY_WORKING);  //활성상태면 활성상태라고 에러 표시
        if(rank==null)throw new BusinessBaseException(ErrorCode.QUEUE_NOT_FOUND); //없으면 없다고 표시


        return rank; //대기순서 반환
    }


    @Transactional
    public boolean registerQueue(QueueRequest queueRequest) throws Exception {
        double score = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        Long userId = queueRequest.getUserId(); //유저정보를 가져와서
        if(!findExpiredAtAndUpdate(userId)) { //활성 대기열에 있는 지확은하고
            longRedisTemplate.opsForZSet().add("waiting_queue", userId, score); //없으면 wait 대기열에 넣고
            return true; //참을 반환
        }

        return false; //활성 대기열에 있으면 못집어넣게 한다.

    }

    public boolean  findExpiredAtAndUpdate(Long userId) {
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens"); //활성 대기열에서 모든 토큰을 가져와서

        if (activeTokens == null || activeTokens.isEmpty()) { //토큰이 없다면 거짓을 반환하고
            return false;
        }

        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        for (String tokenMetaInfo : activeTokens) { //있다면 반복문을 돌면서 주어진 userid 와 같다면 만료시간을 채크하고 만료되지 않았으면 참을 반환
            String[] parts = tokenMetaInfo.split(":");
            if (String.valueOf(userId).equals(parts[0])) {

                Long expiredTime = Long.parseLong(parts[1]);
                if (expiredTime < currentTime) {
                    customStringRedisTemplate.opsForSet().remove("active_tokens", tokenMetaInfo);
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    //결제완료후 제거해주는 로직
    public boolean  findExpiredAtAndUpdate2(Long userId) {
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens"); //활성 대기열에서 모든 토큰을 가져와서

        if (activeTokens == null || activeTokens.isEmpty()) { //토큰이 없다면 거짓을 반환하고
            return false;
        }

        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        for (String tokenMetaInfo : activeTokens) { //있다면 반복문을 돌면서 주어진 userid 와 같다면 
            String[] parts = tokenMetaInfo.split(":");
            if (String.valueOf(userId).equals(parts[0])) {
                //로직완료 후 userId에 해당하는 엑티브 토큰 삭제
                customStringRedisTemplate.opsForSet().remove("active_tokens",tokenMetaInfo);
            }
        }
        return false;
    }

    @Scheduled(cron = "0/20 * * * * *") //20초씩 돌면서 50명을 활성화 시켜주는 코드
    @Transactional
    public void  checkExpiredAtAndUpdateDone(){
        Set<Long> waitingTokens = longRedisTemplate.opsForZSet().range("waiting_queue", 0, 50);
        System.out.println(waitingTokens);

        if (waitingTokens == null || waitingTokens.isEmpty()) {
            return;
        }

        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        for(Long userId: waitingTokens){
            long expirationTime = currentTime + 180;
            String metaInfo = userId+":"+expirationTime;
            System.out.println("metaInfo"+metaInfo);
            customStringRedisTemplate.opsForSet().add("active_tokens", metaInfo);
            longRedisTemplate.opsForZSet().remove("waiting_queue", userId);
        }

    }

    @Scheduled(cron = "0 * * * * *") //테스트하기위해서 매초로 해놨는데 계산을 통해서 스케줄러를 사용하여 반복적으로 만료된 토큰을 제거해주는 코드
    @Transactional
    public void  ExpiredAtAndUpdateDone(){
        Set<String> activeTokens = customStringRedisTemplate.opsForSet().members("active_tokens");

        if(activeTokens == null || activeTokens.isEmpty()){
            return;
        }

        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        for(String tokenMetaInfo: activeTokens){
            String[] parts = tokenMetaInfo.split(":");
            Long expiredTime = Long.parseLong(parts[1]);

            if(expiredTime<currentTime){
                customStringRedisTemplate.opsForSet().remove("active_tokens",tokenMetaInfo);
            }

        }

    }


}
