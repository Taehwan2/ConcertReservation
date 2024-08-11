### MSA 보고서
1. 유저 서비스(로그인 및 포인트 연산) → RDBMS
2. 대기열 서비스(대기열) → Redis
3. 좌석 서비스(좌석 예약) → RDBMS(Batch)
4. 좌석 결제 서비스(좌석 결제 후, 예약 상황 발행) → RDBMS
로 나눌 것 같습니다.


msa 방식을 쓴다면, 서로 외부 서비스가 되기 때문에 

다음과 같은 트랜잭션에 한계가 생길 것이라고 생각합니다.
	**분산 트랜잭션**: 서로 다른 서비스 간의 트랜잭션 일관성 유지가 어려움.
	**네트워크 지연**: 서비스 간 통신이 증가함에 따라 네트워크 지연이 발생할 수 있음.
	**데이터 일관성**: 서비스 간 데이터 동기화 문제.

1. 대기열 등록 및 조회시 유저에 대한 정보를 확인 후 등록하거나 조회하는 부분이 필요하지만 서로 외부의 서비스기 때문에 트랜젝션에 한계를 만날 것이라고 생각.
`@Transactional`은 동일한 애플리케이션 내에서 동작하는 트랜잭션 관리 기능이기 때문에 트랜젝션에 한계를 만날 것이라고 생각.

—> 사가 패턴을 사용하여 트랜잭션 처리

```
public class UserSaga {
private final UserService userService;
private final RedisService redisService;

public UserSaga(UserService userService, RedisService redisService) {
    this.userService = userService;
    this.redisService = redisService;
}

public void enrollUserInQueue(String userId) {
    User user = userService.findByUser(userId);
    if (user != null) {
        try {
            redisService.enrollUser(userId);
        } catch (Exception e) {
            redisService.cancelEnrollUser(userId); // 오류 시 롤백 로직
            throw e;
        }
    }
}

```

}

—> 이벤트를 이용한 처리

---

: 각 로컬에서 트랜잭션을 처리하고, 각각 실패시 보상 트랜잭션을 완료하는 패턴을 사용하면 좋을 것 같다고 생각했습니다.

```
public class UserService {
private final ApplicationEventPublisher eventPublisher;

public UserService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
}

public User findByUser(String userId) {
    // 유저 조회 로직
    User user = ...;
    if (user != null) {
        eventPublisher.publishEvent(new UserFoundEvent(this, userId));
    }
    return user;
}

```

}

2. 대기열은 Redis서버를 통해서 구현되어 있기에 대기열 검증이 필요한 부분은 JWT 토큰을 이용하여 각각 서비스에서 검증하는 것이 좋을 것 같다고 생각했습니다.


3.  좌석 예약 조회 시 1번과 유사하게 
    
    사용자 검증과 좌석 예약 조회를 각각 트랜잭션으로 처리하고 보상 트랜잭션을 적용하는 방식을 사용하여 처리하고, 
    
    3분-5분 동안 예약된 좌석이 결제가 안 될 경우 다시 예약 가능한 상태로 처리하는 것은 spring batch를 사용하여 스케줄 링을 통해 구현하면 좋을 것 같다고 생각했습니다


    좌석 예

- 사가 패턴으로 처리하기

```

public class ReservationSaga {
private final UserService userService;
private final ReservationService reservationService;

public ReservationSaga(UserService userService, ReservationService reservationService) {
    this.userService = userService;
    this.reservationService = reservationService;
}

public Reservation findAndReserveSeat(String userId, String seatId) {
    User user = userService.findByUser(userId);
    if (user != null) {
        try {
            return reservationService.reserveSeat(userId, seatId);
        } catch (Exception e) {
            reservationService.cancelReservation(userId, seatId); 
            throw e;
        }
    }
    return null; // 유저가 없는 경우
}

```

}

- 이벤트로 처리하기

```
public class UserService {
private final ApplicationEventPublisher eventPublisher;

public UserService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
}

public User findByUser(String userId) {
    // 유저 검증 로직
    User user = ...;
    if (user != null) {
        eventPublisher.publishEvent(new UserVerifiedEvent(this, userId));
    }
    return user;
}

}
```

로 나눌 수 있을 것 같다고 생각했습니다.


조회도 마찬가지로
 return reservationService.findReservation(userId); 로 로직을 변경해서 사용하면 될 것이라고 생각합니다.

 4.  결제 
  1.  대기열 조회 → redis & jwt 선 검증
  2. 사용자 조회
  3. 좌석 가격 계산 및 결제
  4. 좌석 상태 변경
  5. 예약서 발행
  6. 결제 여부 리턴

```jsx
public class reserve {
    private final UserService userService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    public BookingSaga(UserService userService, SeatService seatService, 
                       ReservationService reservationService, PaymentService paymentService) {
        this.userService = userService;
        this.seatService = seatService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
    }

    public boolean processBooking(String userId, String seatId, String reservationId) {
        User user = userService.findByUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        try {
            PaymentResult paymentResult = seatService.calculatePriceAndPay(userId, seatId);
            if (!paymentResult.isSuccessful()) {
                throw new RuntimeException("Payment failed");
            }

            try {
                seatService.updateSeatStatus(seatId, SeatStatus.RESERVED);
            } catch (Exception e) {
                seatService.cancelReservation(seatId); // 좌석 상태 변경 실패 시 롤백
                throw e;
            }

            try {
                reservationService.issueReservation(reservationId);
            } catch (Exception e) {
                seatService.cancelReservation(seatId); // 예약서 발행 실패 시 롤백
                throw e;
            }

            try {
                boolean isPaymentSuccessful = paymentService.isPaymentSuccessful(paymentResult.getPaymentId());
                if (!isPaymentSuccessful) {
                    throw new RuntimeException("Payment verification failed");
                }
            } catch (Exception e) {
                seatService.cancelReservation(seatId); // 결제 확인 실패 시 롤백
                throw e;
            }

            return true; // 결제 및 예약 성공
        } catch (Exception e) {
            seatService.cancelReservation(seatId); // 실패 시 롤백
            throw e;
        }
    }
}
```
 



 이 flow인데 모두 메인 로직이므로 사가 패턴을 통해서 처리하고,

안에 메세지 발행같은 부가로직이 있을 경우 이벤트를 통해서 처리하는 것이 좋을 것 같다고 생각했습니다.