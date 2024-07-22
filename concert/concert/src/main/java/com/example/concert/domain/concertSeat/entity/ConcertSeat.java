package com.example.concert.domain.concertSeat.entity;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "concert_seat",
        uniqueConstraints = {
                @UniqueConstraint(name = "concertOptionId_seatNo",
                        columnNames = {"concert_detail_id", "seat_no"}
                )},
        indexes = {
                @Index(name = "idx_concert_detail_id_seat_status", columnList = "concert_detail_id,seat_status"),
                @Index(name = "idx_concertDetailId_seatNo", columnList = "concert_detail_id, seat_no"),
                @Index(name = "idx_seat_status_updated_at", columnList = "seat_status,updated_at")
        }
) //유니크 키를 콘서트 옵션아이디랑, 좌석에 걸어서 한 콘서트에 좌석은 하나만 만들어 질 수 있도록 제약조건 추가
@Getter
@Setter
@Builder
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

   //객체 변환
    public static ConcertSeatResponse entityToResponse(ConcertSeat concertSeat){
        return ConcertSeatResponse.builder()
                .concertSeatId(concertSeat.getConcertSeatId())
                .concertDetailId(concertSeat.getConcertDetailId())
                .userId(concertSeat.getUserId())
                .seatStatus(concertSeat.getSeatStatus())
                .seatNo(concertSeat.getSeatNo())
                .price(concertSeat.getPrice())
                .build();

    }
}
