## 1. redis simple lock
```
    public PointHistory changePoint(PointRequest pointRequest) throws Exception {
        String lockKey = "point:lock"+pointRequest.getUserId();
        String uniqueKey = String.valueOf(System.currentTimeMillis());
        Duration lockDuration = Duration.ofMinutes(1);


        var history = PointHistory.builder().userId(pointRequest.getUserId()).amount(pointRequest.getCharge()).build();
        BigDecimal amount = pointRequest.getCharge();
        // 값을 확인하고 충전인지 사용인지를 확인하는  의 메서드
        history.checkType(amount);
       //userId 에 맞는 포인트를 가져와서 값을 계산하는 로직

        boolean lock = lockService.getLock(lockKey,uniqueKey,lockDuration);
        log.info("====> here is lock ");
        if(!lock){
            throw new Exception("you don't have lock");
        }
        try {
            log.info("I have a lock");
            userService.calculate(pointRequest.getUserId(), amount);
        }catch (Exception e){
            log.info("Excetpion"+e);
        }finally {
            lockService.outLock(lockKey,uniqueKey);
        }
        return pointHistoryService.save(history);
    }
}


@SpringBootTest
@EnableRetry
public class UserPointCurrencyTest {
@Autowired
private UserService userService;

@Autowired
private UserPointFacade userPointFacade;

private static final int THREAD_COUNT = 3;

@BeforeEach
void beforeEach() throws Exception {
    userService.save(new User(1L,"taehwan",new BigDecimal(1000)));
}

@Test
public void testConcurrentSeatReservation() throws InterruptedException {
    System.out.println(userService.getUserPoint(1L).getName());
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
    int random = new Random().nextInt(10000);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < THREAD_COUNT; i++) {
        executorService.submit(() -> {
            try {
                userPointFacade.changePoint(new PointRequest(1L,new BigDecimal(5000)));
                System.out.println(userService.getUserPoint(1L).getPoint());
            } catch (OptimisticLockingFailureException e){
                System.out.println("여기걸림");
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });
    }

    latch.await();
    executorService.shutdown();
    System.out.println(userService.getUserPoint(1L).getPoint());

    long endTime = System.currentTimeMillis(); // 종료 시간 측정
    System.out.println("Execution time: " + (endTime - startTime) + " ms"); // 실행 시간 출력
}


```

![4](https://github.com/user-attachments/assets/bd625b37-ba30-4f38-bafc-457965deccb6)

3명에 10000원씩 넣는다.
  redis에서 lock을 걸고 userPoint를 등록하는 부분에 transaction을 걸어놓고 실행을 하게되면 
248초 걸리지만 락을 획득하지 못하는 애들은 한번더 로직을 실행하지 못한다. 그렇기에 모든 로직을 수행하지 못하고 끝난다. sprin lock을 사용해보겠다.

## spin lock
```
    Service
@RequiredArgsConstructor
public class LockService {
    private final RedisClient redisTemplate;
    private final StatefulRedisConnection<String,String> connection;
    private final RedisCommands<String,String> commands;



    public boolean getLock(String lockKey, String uniqueValue, Duration duration,int maxRetires, long retryIntervalMills){
        int retires = 0;
        while(retires<maxRetires){
            Boolean lockAcquired = commands.setnx(lockKey,uniqueValue);
            if(lockAcquired!=null && lockAcquired){
                commands.expire(lockKey,duration.getSeconds());
                return  true;
            }
            retires ++;
            try{
                TimeUnit.MILLISECONDS.sleep(retryIntervalMills);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    public void outLock(String lockKey,String uniqueValue){
        String nowVal = commands.get(lockKey);
        if(uniqueValue.equals(nowVal)){
            commands.del(lockKey);
        }
    }

    public void close(){
        connection.close();
        redisTemplate.shutdown();
    }


}
```

![5](https://github.com/user-attachments/assets/eea6d2c2-e998-4e17-a3ed-75baf2d1cd23)


레튜스 라이브러리를 이용하여 재시도를 하는 로직을 통해서 했더니 그래도 꽤 빠른 속도로 모든 
로직이 순차적으로 수정되는 것을 볼 수 있었다.

## pub/sub 아직 잘 이해가안간다. 어떤게 장점이고 단점인지
```
public PointHistory changePoint(PointRequest pointRequest) throws Exception {
    String lockKey = "point:lock"+pointRequest.getUserId();
    String uniqueKey = String.valueOf(System.currentTimeMillis());
    Duration lockDuration = Duration.ofMinutes(1);


    var history = PointHistory.builder().userId(pointRequest.getUserId()).amount(pointRequest.getCharge()).build();
    BigDecimal amount = pointRequest.getCharge();
    // 값을 확인하고 충전인지 사용인지를 확인하는  의 메서드
    history.checkType(amount);
   //userId 에 맞는 포인트를 가져와서 값을 계산하는 로직
    boolean lock = lockService.tryLock(lockKey,uniqueKey,lockDuration,10,5L);
    if(!lock){
        lockService.waitForLock(lockKey);
        lock = lockService.tryLock(lockKey,uniqueKey,lockDuration,10,5L);
        if(!lock){
            throw new Exception("NO lock");
        }
    }

    try {
        log.info("I have a lock");
        userService.calculate(pointRequest.getUserId(), amount);
    }catch (Exception e){
        log.info("Excetpion"+e);
    }finally {
        lockService.outLock(lockKey,uniqueKey);
    }
    return pointHistoryService.save(history);
}


```

![4](https://github.com/user-attachments/assets/0184bf84-2122-4689-ba0b-9c3db2bcc9fe)


PubSub방식으로 했을 때 wait로직을 잘 작성해야하고, 시간을 잘 맞춰야 정상적으로 모두 돌아가는 것같고

시간차이는 크지않았다.
