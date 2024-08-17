package com.example.concert.Presentation.concert.model.seat;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConcertSeatRequest.class,name = "concertSeat")
})
public class ConcertSeatRequest {
    Long userId;
    Long concertDetailId;
    int seatNo;
}
