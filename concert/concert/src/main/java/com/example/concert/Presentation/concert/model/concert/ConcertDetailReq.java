package com.example.concert.Presentation.concert.model.concert;

import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Configuration
//역직렬화를 위한 선언
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConcertDetailReq.class, name = "concertDetailReq")
})
public class ConcertDetailReq {
    //시작일
    private LocalDateTime startDate;
    //예약시작일
    private LocalDateTime reservationStartDate;

}
