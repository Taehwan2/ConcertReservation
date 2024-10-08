## 1. lock 안걸었을 때

![스크린샷 2024-07-25 오후 9 20 12](https://github.com/user-attachments/assets/dca78cea-8da3-4e06-bfc4-732c947fc127)
아래와같이 하나의 트렌젝션만 insert를 하게되고 나머지는 제약에 걸려서 실패하게 되서 정상 동작하게 된다.


## 2. 비관적 락 구현
공유락은 데이터를 반복 읽기만 하고 수정하지 않을 때 사용한다. 
계속적으로 데이터의 변함이없어야하는데 -> update를 해야함으로 적절하지 않다고 판단했다.
데이터베이스 대부분은 방언에 의해 PESSIMISTIC_WRITE 로 동작한다.
보장이 안되기에 쓰기락을 사용했다.
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
한번씩 실행되고 430초가 걸려서 비관적 락보음>
하지만 여기서 판단해야할 점은, 이 좌석은 한명이 예약하면 다른 사람이 예약을 못하는 로직임으로 재처리가 필요없다 그렇기 때문에 재처리 로직을 제외하고 실행한다면


![image](https://github.com/user-attachments/assets/fc52ac41-c597-414a-8db7-a35fd3293137)

** 시간도 확 주는 것을 확인할 수 있고 update부분에서 여러명이 한번에 좌석을 업데이트를 방지하는 것을 볼 수 있어. 좌석 예약은 낙관적 락을 통해서 구현했다. **
