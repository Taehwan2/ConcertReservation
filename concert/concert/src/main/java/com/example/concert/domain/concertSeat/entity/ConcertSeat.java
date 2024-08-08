package com.example.concert.domain.concertSeat.entity;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

@Entity
//index 설정
@Table(name = "concert_seat",
        uniqueConstraints = {
                @UniqueConstraint(name = "concertOptionId_seatNo",
                        columnNames = {"concert_detail_id", "seat_no"}
                )},

        indexes = {

                @Index(name = "idx_concert_detail_id_seat_status", columnList = "concert_detail_id,seat_status"),  //좌석을 조회 할때 예약된 좌석 빼고 모두 불러오기위해
                @Index(name = "idx_concertDetailId_seatNo", columnList = "concert_detail_id,seat_no"), //좌석을 등록할때 그 좌석에 대한 정보를 가져오기 위한 쿼리의 인덱스
                @Index(name ="idx_userId",columnList = "user_id") //유저가 예약한 예약 좌석을 모두 들고오기위한 쿼리의 인덱스
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
    @Version    //낙관적 락을 위해 version정보 삽입
    private int version;

    public ConcertSeat(long l, long l1, long l2, SeatStatus reservable, int i, BigDecimal bigDecimal) {
        super();
    }


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
