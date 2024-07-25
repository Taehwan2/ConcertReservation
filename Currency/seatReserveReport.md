## 1. lock 안걸었을 때

![스크린샷 2024-07-25 오후 9 20 12](https://github.com/user-attachments/assets/dca78cea-8da3-4e06-bfc4-732c947fc127)
아래와같이 하나의 트렌젝션만 insert를 하게되고 나머지는 제약에 걸려서 실패하게 되서 정상 동작하게 된다.


## 2. 비관적 락 구현

```
  @Transactional
    public ConcertSeat reserveSeatTemp(ConcertSeatRequest concertSeatRequest) throws Exception {
        Long userId = concertSeatRequest.getUserId();
        Long concertDetailId = concertSeatRequest.getConcertDetailId();
        int seatNo = concertSeatRequest.getSeatNo();
        if(seatNo<0 || seatNo>SEAT_LIMIT)throw new BusinessBaseException(ErrorCode.SEAT_NO_INVALID);
        //유니크 제약 조건이걸린 콘서트 속성 과 좌석 번호를 가져와서
        ConcertSeat concertSeat = seatRepository.findSeat(concertDetailId,concertSeatRequest.getSeatNo());
        //없는 좌석이면 새롭게 추가를 하고
        if(concertSeat==null){
            var request = ConcertSeat.builder().concertDetailId(concertDetailId)
                    .userId(userId)
                    .seatNo(seatNo)
                    .seatStatus(SeatStatus.TEMP)
                    .price(new BigDecimal(seatNo*1000))
                    .build();
            
            return seatRepository.createSeat(request);
        } //있는 좌석이면 userId 랑 상태만 추가해서 사용한다.
        if(concertSeat.getSeatStatus() == SeatStatus.RESERVABLE){
            concertSeat.setSeatStatus(SeatStatus.TEMP);
            concertSeat.setUserId(userId);
            seatRepository.updateSeat(concertSeat);
            return concertSeat;
        }
        throw new Exception("CAN'T SEAT TO RESERVE");
    }


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ConcertSeat findByConcertDetailIdAndSeatNo(Long concertDetailId, int seatNo);


    @Modifying
    @Query("UPDATE ConcertSeat c set c.seatStatus = :reservable , c.userId = :userId WHERE c.concertSeatId =:seatNo")
    int updateSeatStatusAndUserId(Long userId, SeatStatus reservable, Integer seatNo);
```
![2](https://github.com/user-attachments/assets/6a0a842a-207f-443e-a889-ddf616da3641)

비관적 락을 걸면 어쩌피 Logic에서 한 사람이 예약한 좌석을 예약할 순 없지만, 
한번씩 실행되고 430초가 걸려서 비관적 락보다는 훨씬 유용하다.

##3. 낙관적 락 구현

```
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertSeatId;
    private  Long concertDetailId; //콘서트 옵션아이디
    private Long userId; //유저 아이디
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus; //좌석 상태
    private Integer seatNo; //좌석 번호
    private BigDecimal price; //좌석 가격
    @Version
    private Integer version;



    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public ConcertSeat reserveSeatTemp(ConcertSeatRequest concertSeatRequest) throws Exception {
        Long userId = concertSeatRequest.getUserId();
        Long concertDetailId = concertSeatRequest.getConcertDetailId();
        int seatNo = concertSeatRequest.getSeatNo();
        if(seatNo<0 || seatNo>SEAT_LIMIT)throw new BusinessBaseException(ErrorCode.SEAT_NO_INVALID);
        //유니크 제약 조건이걸린 콘서트 속성 과 좌석 번호를 가져와서
        ConcertSeat concertSeat = seatRepository.findSeat(concertDetailId,concertSeatRequest.getSeatNo());
        //없는 좌석이면 새롭게 추가를 하고
        if(concertSeat==null){
            var request = ConcertSeat.builder().concertDetailId(concertDetailId)
                    .userId(userId)
                    .seatNo(seatNo)
                    .seatStatus(SeatStatus.TEMP)
                    .price(new BigDecimal(seatNo*1000))
                    .build();
            
            return seatRepository.createSeat(request);
        } //있는 좌석이면 userId 랑 상태만 추가해서 사용한다.
        if(concertSeat.getSeatStatus() == SeatStatus.RESERVABLE){
            concertSeat.setSeatStatus(SeatStatus.TEMP);
            concertSeat.setUserId(userId);
            seatRepository.updateSeat(concertSeat);
            return concertSeat;
        }
        throw new Exception("CAN'T SEAT TO RESERVE");
    }

```
![3](https://github.com/user-attachments/assets/c2bc1ae2-4a37-46e0-8ae5-b8a331596729)

Entity에 버전을 걸고 
낙관적락을 걸고 테스트를 실행하면 

재시도 횟수에 맞춰서 낙관적 락에 걸린 애들은  여러번 재시도하게되고 시간초도 저렇게 많이 걸리게 된다.

한번 update되면 아무리 재시도해도 좌석예약이 이미 바뀌어있기에 낭비만 되게된다.

시간도 1000초로 훨씬 오래걸린다.
