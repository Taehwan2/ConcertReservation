1. 포인트 락 없이 오로지 트랜잭션으로 실행

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
