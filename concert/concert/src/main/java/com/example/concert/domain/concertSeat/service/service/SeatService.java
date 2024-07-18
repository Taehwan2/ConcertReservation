package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SeatService {

    private  final Integer SEAT_LIMIT = 10; //좌석 최대 10자리 까지 가능
    private final Integer SEAT_TIME = 5;  //임시 예약 5분까지 가능
    private final SeatRepository seatRepository;
    

    public List<ConcertSeat> FindAbleSeats(Long concertDetailId) throws Exception {
        List<ConcertSeat> reservedSeats = seatRepository.findStatusReserved(concertDetailId, SeatStatus.RESERVABLE);  //예약가능한 자리 빼고 불러오는 리스트
        checkSize(reservedSeats.size());  //예약된 자리의 크기를 검증하는 메서드
        System.out.println("size is"+reservedSeats.size());
        List<ConcertSeat> concertSeats = getAbleSeats(reservedSeats); //예약가능한 자리를 반환하는 메서드
        return concertSeats;
    }

    public List<ConcertSeat> getAbleSeats(List<ConcertSeat> reservedSeats) {
        Set<Integer> reservedSeatNumbers = reservedSeats.stream()
                .map(ConcertSeat::getSeatNo)
                .collect(Collectors.toSet());   //예약되어있거나 결제되어있는 자리들의 아이디를 받아서
        return IntStream.rangeClosed(1,SEAT_LIMIT)
                .filter(a-> !reservedSeatNumbers.contains(a))
                .mapToObj(a-> ConcertSeat.builder()
                        .seatNo(a)
                        .price(BigDecimal.valueOf(a*1000))
                        .build()
                ).toList();  //예약할 수 있는 자리의 수 중 예약된 자리만 뺴고 반환
    }

    public void checkSize(int size) throws Exception {  //자리 크기 체크
        if(size == SEAT_LIMIT) throw new Exception("ALREAY FULL SEAT");
    }

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
            return  seatRepository.createSeat(concertSeat);
        }
        throw new Exception("CAN'T SEAT TO RESERVE");
    }

    @Scheduled(cron = "0 * * * * *") //스케줄러 페키지를 만들고 거기로 이동 예정
    @Transactional
    public void checkExpiredSeat(){
        //잠시 예약되어있는 좌석중 만료시간이 넘은 좌석들을 불러와서 상태를 변경시켜준다.
        List<ConcertSeat> concertList = seatRepository.findExpiredInTemp(SeatStatus.TEMP, LocalDateTime.now().minusMinutes(SEAT_TIME));
        if (concertList == null || concertList.isEmpty()) {
            return;
        }
        List<Long> concertId = concertList.stream()
                .map(ConcertSeat::getConcertSeatId).toList();
          seatRepository.changeSeatsStatus(concertId);

    }
    //좌석 결제에서 사용하는 메서드로 예약되어있는 좌석가져오기
    public List<ConcertSeat> findTempSeatByUserId(Long userId) {
    return seatRepository.findTempSeatByUserId(userId);
    }

    @Transactional  //좌석들을 모두 결제된 상태로 바꾸는 로직
    public List<ConcertSeat> updatedSeatToReserved(Long userId, List<ConcertSeat> tempSeats) {

        tempSeats.forEach(a->a.setSeatStatus(SeatStatus.RESERVED));

        List<Long> seatIds = tempSeats.stream()
                .map(ConcertSeat::getConcertSeatId).toList();

        seatRepository.updatedSeatToReserved(userId,seatIds);
        return tempSeats;
    }
}
