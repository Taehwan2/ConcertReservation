    package com.example.concert.Presentation.concert.model.queue;


    import com.example.concert.Presentation.concert.model.concert.ConcertReq;
    import com.fasterxml.jackson.annotation.JsonSubTypes;
    import com.fasterxml.jackson.annotation.JsonTypeInfo;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    //역직렬화 예방을 위한 선언
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = QueueRequest.class,name = "queueReq")
    })
    public class QueueRequest {
        private Long userId;

    }
