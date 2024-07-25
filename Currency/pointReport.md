## 1. 포인트 락 없이 오로지 트랜잭션으로 실행

```
@Transactional
    public int calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserbyUserId(userId);
        userPoint.calculate(amount);
        //계산된 포인트를 저장하고 결과를  pointHistory 저장하는 로직
        return updateUserPoint(userPoint);
    }
```
조회 부분에서 Lock 을 걸지 않고, @Transactional만 걸었을 경우에는 동시성 처리에 문제가 생긴다.

동시에 여러 Transaction 들이 하나의 API를 요청했을 때 동시에 같은 값을 조회하고 동시에 처리하기 때문에
동시성을 50 걸고 처리해도 한명, 두명 정도의 값만 바뀌고 다 동일값을 엎어친다.

기본적인 transaction만 걸었을 경우 Logic 실행 중에 조회가 가능하기에 값의 일관성을 보장 할 수 없다는 단점이 있다.

시간으로 측정했을 때는 675초가 나오게 된다.


![1](https://github.com/user-attachments/assets/6f5b8f0a-c79a-4541-912f-e55a206a7de4)

-----------------------------------------------------------------------------------------------------------------------------------------
## 2. 비관적 락 사용
공유락은 데이터를 반복 읽기만 하고 수정하지 않을 때 사용한다. 
데이터베이스 대부분은 방언에 의해 PESSIMISTIC_WRITE 로 동작한다.
보장이 안되기에 쓰기락을 사용했다.
```
    @Transactional
    public int calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserWithLock(userId);
        userPoint.calculate(amount);
        //계산된 포인트를 저장하고 결과를  pointHistory 저장하는 로직
        return updateUserPoint(userPoint);
    }
    
     public User getUserWithLock(Long userId){return userRepository.getUserPointWithLock(userId);}

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByIdWithLock(@Param("userId") Long userId);


```

아래와 같이 비관적 Lock을 걸었을 때는 lock을 걸어서 순차적으로 처리하기 때문에 
정확하게 값이 나온다.

![2](https://github.com/user-attachments/assets/b5b6882b-42a2-426a-941e-15d6440226b7)

217ms 정도 나오고 값이 정확하게 51000이 나온다.

## 3. 낙관적 락 사용
```
 @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public User calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserPoint(userId);
        userPoint.calculate(amount);
        return save(userPoint);
    }


    public class User  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    //사용자 이름
    private String name;
   //사용자 포인트
    private BigDecimal point;
    //포인트 변환하는 로직

    @Version
    private Integer version;


     @Transactional
    public int calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserWithLock(userId);
        userPoint.calculate(amount);
        //계산된 포인트를 저장하고 결과를  pointHistory 저장하는 로직
        return updateUserPoint(userPoint);
    }
```
Entity에 int로 version 정보를 추가하고
시도 로직까지 구현하여 비관적락과 동일한 상태로 테스트 해봤을 때 시간이 2232ms걸린거보면 10배이상의 속도가 차이나고 쿼리도 훨씬 많이 사용된걸로 확인되어 비관적락이 더 효율적이라는 것을 볼 수 있다.

![3](https://github.com/user-attachments/assets/e8f6cc85-d84f-4353-a510-56c043d43db7)
