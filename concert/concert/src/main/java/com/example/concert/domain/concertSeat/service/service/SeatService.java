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

    private  final Integer SEAT_LIMIT = 10;
    private final Integer SEAT_TIME = 5;
    private final SeatRepository seatRepository;
    

    public List<ConcertSeat> FindAbleSeats(Long concertDetailId) throws Exception {
        List<ConcertSeat> reservedSeats = seatRepository.findStatusReserved(concertDetailId, SeatStatus.RESERVABLE);
        checkSize(reservedSeats.size());
        System.out.println("size is"+reservedSeats.size());
        List<ConcertSeat> concertSeats = getAbleSeats(reservedSeats);
        return concertSeats;
    }

    public List<ConcertSeat> getAbleSeats(List<ConcertSeat> reservedSeats) {
        Set<Integer> reservedSeatNumbers = reservedSeats.stream()
                .map(ConcertSeat::getSeatNo)
                .collect(Collectors.toSet());
        return IntStream.rangeClosed(1,SEAT_LIMIT)
                .filter(a-> !reservedSeatNumbers.contains(a))
                .mapToObj(a-> ConcertSeat.builder()
                        .seatNo(a)
                        .price(BigDecimal.valueOf(a*1000))
                        .build()
                ).toList();
    }

    public void checkSize(int size) throws Exception {
        if(size == SEAT_LIMIT) throw new Exception("ALREAY FULL SEAT");
    }

    @Transactional
    public ConcertSeat reserveSeatTemp(ConcertSeatRequest concertSeatRequest) throws Exception {
        Long userId = concertSeatRequest.getUserId();
        Long concertDetailId = concertSeatRequest.getConcertDetailId();
        int seatNo = concertSeatRequest.getSeatNo();
        if(seatNo<0 || seatNo>SEAT_LIMIT)throw new BusinessBaseException(ErrorCode.SEAT_NO_INVALID);

        ConcertSeat concertSeat = seatRepository.findSeat(concertDetailId,concertSeatRequest.getSeatNo());

        if(concertSeat==null){
            var request = ConcertSeat.builder().concertDetailId(concertDetailId)
                    .userId(userId)
                    .seatNo(seatNo)
                    .seatStatus(SeatStatus.TEMP)
                    .price(new BigDecimal(seatNo*1000))
                    .build();
            
            return seatRepository.createSeat(request);
        }
        if(concertSeat.getSeatStatus() == SeatStatus.RESERVABLE){
            concertSeat.setSeatStatus(SeatStatus.TEMP);
            concertSeat.setUserId(userId);
            return  seatRepository.createSeat(concertSeat);
        }
        throw new Exception("CAN'T SEAT TO RESERVE");
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkExpiredSeat(){
        
        List<ConcertSeat> concertList = seatRepository.findExpiredInTemp(SeatStatus.TEMP, LocalDateTime.now().minusMinutes(SEAT_TIME));
        if (concertList == null || concertList.isEmpty()) {
            return;
        }
        List<Long> concertId = concertList.stream()
                .map(ConcertSeat::getConcertSeatId).toList();
          seatRepository.changeSeatsStatus(concertId);

    }

    public List<ConcertSeat> findTempSeatByUserId(Long userId) {
    return seatRepository.findTempSeatByUserId(userId);
    }

    @Transactional
    public List<ConcertSeat> updatedSeatToReserved(Long userId, List<ConcertSeat> tempSeats) {

        tempSeats.forEach(a->a.setSeatStatus(SeatStatus.RESERVED));

        List<Long> seatIds = tempSeats.stream()
                .map(ConcertSeat::getConcertSeatId).toList();

        seatRepository.updatedSeatToReserved(userId,seatIds);
        return tempSeats;
    }
}
