/*
package com.example.concert.redis;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LockService {

        private final RedisCommands<String, String> commands;
        private final RedisPubSubCommands<String, String> pubSubCommands;
        private final StatefulRedisPubSubConnection<String, String> pubSubConnection;

        public boolean tryLock(String lockKey, String uniqueValue, Duration duration, int maxRetries, long retryIntervalMillis) {
            int retries = 0;
            while (retries < maxRetries) {
                Boolean lockAcquired = commands.setnx(lockKey, uniqueValue);
                if (lockAcquired != null && lockAcquired) {
                    commands.expire(lockKey, duration.getSeconds());
                    return true;
                }
                retries++;
                try {
                    TimeUnit.MILLISECONDS.sleep(retryIntervalMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            return false;
        }

        public void outLock(String lockKey, String uniqueValue) {
            String currentValue = commands.get(lockKey);
            if (uniqueValue.equals(currentValue)) {
                commands.del(lockKey);
                pubSubCommands.publish("lock:released", lockKey);
            }
        }

        public void waitForLock(String lockKey) {
            pubSubConnection.addListener(new LockListener(lockKey));
            pubSubCommands.subscribe("lock:released");
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private class LockListener extends io.lettuce.core.pubsub.RedisPubSubAdapter<String, String> {
            private final String lockKey;

            public LockListener(String lockKey) {
                this.lockKey = lockKey;
            }

            @Override
            public void message(String channel, String message) {
                if (channel.equals("lock:released") && message.equals(lockKey)) {
                    synchronized (LockService.this) {
                        LockService.this.notify();
                    }
                }
            }
        }

}

*/
