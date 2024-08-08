## 메인로직과 부가로직의 분리 보고서

#### 현재 좌석 졀제의  flow

1. userId를 통해 유저를 검증한다.
2. concertDetailId로 콘서트의 옵셥 정보를 확인한다.
3. 사용자가 예약한 좌석을 모두 한번에 불러온다.
4. 결제를 시도하고 잔액이 부족할 경우 에러를 발생시킨다.
5. 결제가 완료되었으면, 좌석의 상태를 Temp에서 결제 완료 상태로 변경시킨다.
6. 예약 좌석당 예약정보를 저장하고
7. 결제가 완료되었다는 정보를 return 한다.


모두 하나의 Transaction 으로 걸려있기 때문에 모두가 main logic 이다.
단 하나라도 실패하면 안되기 때문이다.

하지만 실시간 예약 메세지 발송이라는 부가로직이 들어간다면?
```
    @Transactional
    public Payment concertPayment(ConcertSeatRequest concertSeatRequest) throws Exception {
        // 임시 예약좌석 불러오기.
        Long userId = concertSeatRequest.getUserId();
        ConcertDetail concertDetail = concertDetailService.getConcertDetail(concertSeatRequest.getConcertDetailId());
        List<ConcertSeat> tempSeats = seatService.findTempSeatByUserId(userId);
        BigDecimal payment = tempSeats.stream().map(ConcertSeat::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        //결제.
        PointRequest pointRequest = new PointRequest(userId,payment);
        var userPoint = userService.getUserPoint(pointRequest.getUserId());
        userPoint.calculate(payment);
        userService.save(userPoint);

        //예약좌석 업데이트.
        List<ConcertSeat> paymentSeat = seatService.updatedSeatToReserved(userId,tempSeats);


        paymentSeat.forEach(concertSeat -> {
            Reservation reservation = Reservation.builder()
                    .seatId(concertSeat.getConcertSeatId())
                    .userId(userId)
                    .concertDetailId(concertSeatRequest.getConcertDetailId())
                    .concertId(concertDetail.getConcertId())
                    .build();

            reservationService.saveReservation(reservation);

            // 예약 완료 이벤트 발행
           // eventPublisher.publishEvent(new ReservationCompletedEvent(this, reservation));
        });
        messageService.send(userId);

        var pay = Payment.builder().check(true).build();
        //예약 반환.

        redisQueueService.findExpiredAtAndUpdate2(userId);
        return pay;
    }

```

```
    @Service
    public class MessageService {
        void send(Long userId) throws InterruptedException {
            Thread.sleep(3000L); // 3초 정지
            if (userId > 0) throw new RuntimeException();
         }
}
```

생기는 상황:
    만약 메세지 외부 서비스가 (3초 정지 5초 정지 길면 1분 정지를 걸어 놓으면) 결제 메서드가 그만큼 오래 걸리게 되고 사용자는 하염 없이 기다려야 하는 상황이 생긴다.
    또한 만약 메세지를 보내는 외부 서비스가 실패한다면 부가로직임에도 불구하고 예약 정보 또한 저장되지 않고 모두 Rollback 되는 원하지 않는 error 상황이 발생한다는 것을 파악했습니다.


<API>요청

![1](https://github.com/user-attachments/assets/184e32be-a8e7-476f-b159-f410c48fe520)



<Rollback> 상황

![2](https://github.com/user-attachments/assets/a48b9060-7310-4a29-bd9a-f110d1779952)

이러한 상황을 해결하기 위해서 부가로직을 EventListener 를 통해서 해결하기로 했다.
### 해결 EVENTLISTENER
```
     @Transactional
    public Payment concertPayment(ConcertSeatRequest concertSeatRequest) throws Exception {
        // 임시 예약좌석 불러오기.
        Long userId = concertSeatRequest.getUserId();
        ConcertDetail concertDetail = concertDetailService.getConcertDetail(concertSeatRequest.getConcertDetailId());
        List<ConcertSeat> tempSeats = seatService.findTempSeatByUserId(userId);
        BigDecimal payment = tempSeats.stream().map(ConcertSeat::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        //결제.
        PointRequest pointRequest = new PointRequest(userId,payment);
        var userPoint = userService.getUserPoint(pointRequest.getUserId());
        userPoint.calculate(payment);
        userService.save(userPoint);

        //예약좌석 업데이트.
        List<ConcertSeat> paymentSeat = seatService.updatedSeatToReserved(userId,tempSeats);


        paymentSeat.forEach(concertSeat -> {
            Reservation reservation = Reservation.builder()
                    .seatId(concertSeat.getConcertSeatId())
                    .userId(userId)
                    .concertDetailId(concertSeatRequest.getConcertDetailId())
                    .concertId(concertDetail.getConcertId())
                    .build();

            reservationService.saveReservation(reservation);

            // 예약 완료 이벤트 발행
            eventPublisher.publishEvent(new ReservationCompletedEvent(this, reservation));
        });


        var pay = Payment.builder().check(true).build();
        //예약 반환.

        redisQueueService.findExpiredAtAndUpdate2(userId);
        return pay;
    }

```

```
    @Getter
    public class ReservationCompletedEvent extends ApplicationEvent {
        private final Reservation reservation;

        public ReservationCompletedEvent(Object source, Reservation reservation) {
            super(source);
            this.reservation = reservation;
        }
}
```

```
    @Component
    @RequiredArgsConstructor
    public class ReservationCompletedEventListener {
        private final ExternalApiClient externalApiClient;

        //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        @EventListener
        public void handleReservationCompletedEvent(ReservationCompletedEvent event) throws InterruptedException {

            System.out.println("Handling reservation completed event for reservation: " + event.getReservation().getReservationId());
            externalApiClient.sendOrderResult(event.getReservation());
        }
    }
```
```
    @Service
    public class ExternalApiClient {
        public void sendOrderResult(Reservation reservation) throws InterruptedException {

            Thread.sleep(3000L); // 3초 정지
            System.out.println("Sending order result to external API for reservation: " + reservation.getReservationId());
            throw new RuntimeException();
        }
    }
```

reservation 객체에 이벤트를 걸어서, 이벤트를 처리하고

@EventListener 를 통해서 이벤트를 발생시키게 되면.

아래 결과와 같이 에러를 볼 수 있습니다.

![3](https://github.com/user-attachments/assets/74c5eeba-24e3-4727-8a46-1bbb171d87c9)



그이유는 @EventListener는 트랜잭션과 아예 독립적으로 작동하기 때문에 트랜잭션이 완료되지 않은 상태에서 이벤트에서 에러가난다면 에러가 터져서 커밋하기 전에 에러가 처리되기 때문입니다


그렇기에 @TransactionalEventListener

- `BEFORE_COMMIT`: 트랜잭션이 커밋되기 전에 이벤트를 처리합니다.
- `AFTER_COMMIT`: 트랜잭션이 성공적으로 커밋된 후에 이벤트를 처리합니다.
- `AFTER_ROLLBACK`: 트랜잭션이 롤백된 후에 이벤트를 처리합니다.
- `AFTER_COMPLETION`: 트랜잭션 완료 후에 이벤트를 처리합니다.

를 찾아보았고, 

- `AFTER_ROLLBACK`   → 이부분은 로직과 관계가 없을 것 같아서 패스를 했고,

- `BEFORE_COMMIT` 이 부분을 사용하면 커밋 되기 전이기 때문에 에러가 난다면 
모두 roll back 되게 된다.

![5](https://github.com/user-attachments/assets/19fd30f5-109c-42c5-994b-d3fc3efdfff9)
![6](https://github.com/user-attachments/assets/69178cf7-15eb-45eb-a1c4-fce46d0a869e)



AFTER_COMMIT을 사용해서 트랜잭션이 커밋된 후에  이벤트를 처리하게 되면
외부 api가 에러가 난다해도 이미 커밋된 후이기 때문에 roll back 되지 않는 것을 확인 해 볼 수 있다.


![7](https://github.com/user-attachments/assets/5ec09324-0911-4210-83e8-c9076584d70c)
![8](https://github.com/user-attachments/assets/29154910-b086-4462-a5e7-62ec090e7e0b)


```
    @Component
    @RequiredArgsConstructor
    public class ReservationCompletedEventListener {
        private final ExternalApiClient externalApiClient;

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        //@EventListener
        public void handleReservationCompletedEvent(ReservationCompletedEvent event) throws InterruptedException {

            System.out.println("Handling reservation completed event for reservation: " + event.getReservation().getReservationId());
            externalApiClient.sendOrderResult(event.getReservation());
        }
    }
```

![9](https://github.com/user-attachments/assets/85b545f6-74d3-4835-b0be-bcea1f464de0)

아래와 같이 성공하는 것을 알 수 있습니다.

하지만 

- `BEFORE_COMMIT`: 트랜잭션이 커밋되기 전에 이벤트를 처리합니다.
- `AFTER_COMMIT`: 트랜잭션이 성공적으로 커밋된 후에 이벤트를 처리합니다.
- `AFTER_COMPLETION`: 트랜잭션 완료 후에 이벤트를 처리합니다.

이 세가지를 비교해 보았을 때 예약이 완료되어야만 이벤트가 발행되어야하기 때문에

 `BEFORE_COMMIT` 와 `AFTER_COMPLETION` 는 부적합하다고 생각 되어 

AFTER_COMMIT을 택했습니다.

하지만 외부로직이 오래걸릴 때 기존 트랜잭션에 영향이 있으면 안되는 로직으로 했는데

이벤트가 발생하여 Thread.sleep()을 걸면 기본 트랜잭션도 걸린다..

원인:

ApplicationEvent 가 발행되면, 핸들링할 수 있는 리스너 있는지 찾아서 스프링이 "실행"시켜준다고 했죠?

그 "실행" 하는 로직이 같은 스레드에서 수행되므로 같은 스레드입니다.

```
    @Component
    @RequiredArgsConstructor
    public classReservationCompletedEventListener{
    private finalExternalApiClientexternalApiClient;
    @Async
        @TransactionalEventListener(phase =TransactionPhase.AFTER_COMPLETION)
    //@EventListener
    public voidhandleReservationCompletedEvent(ReservationCompletedEventevent)throwsInterruptedException{

    System.out.println("Handling reservation completed event for reservation: " +event.getReservation().getReservationId());
            externalApiClient.sendOrderResult(event.getReservation());
        }
    }
```



비동기 처리를 하니 다른 쓰레드로 실행해서 기존 트랜잭션이 영향을 안받는다.

### 결론
메인로직과 부가로직을 판단하는 것은. 
한 트랜잭션 안에서 실패시 모두 롤벡이 되어야한다? 괜찮다? 로 나눌 수 있고.
실시간 예약정보 전송 같은 서비스는 부가로직이된다. 
그렇기에 event를 통해서 트랜잭션에서 독립시킬수 있다.
하지만 eventListener에는 기본과 transactionEventListener가 있는데
eventListener는 트랜잭션에 완전히 독립적이고 transactionEventListener는 트랜잭션안에 포함되어있을 때 event를 처리해준다.
지금 상황은 아래와 같은 상황이므로 transactionEventListener를 선택해주었고, 같은 쓰레드를 사용하면 안되기 때문에 비동기 처리를 해주었다. @ASYNC 를 통해
transactionEventListenerd에는 
- `BEFORE_COMMIT`: 트랜잭션이 커밋되기 전에 이벤트를 처리합니다.
- `AFTER_COMMIT`: 트랜잭션이 성공적으로 커밋된 후에 이벤트를 처리합니다.
- `AFTER_ROLLBACK`: 트랜잭션이 롤백된 후에 이벤트를 처리합니다.
- `AFTER_COMPLETION`: 트랜잭션 완료 후에 이벤트를 처리합니다.
아래 네가지 경우가있는데 커밋전에 이벤트처리 X 롤벡 후 이벤트처리 X 는 포함이 아예 안되는 상황이고,
AFTER_COMPLETION는 트랜잭션이 실패하거나 롤백을 해도 수행하기에 맞지 않는다고 생각했다. (예약이 실패했는데 예약메세지 전송을?)
그렇기에 AFTER_COMMIT를 체택하여 사용했다.