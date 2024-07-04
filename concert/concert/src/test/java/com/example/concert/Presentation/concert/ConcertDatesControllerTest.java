package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertFacade;
import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.domain.concertdetail.ConcertDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class ConcertDatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcertFacade concertFacade;

    @Test
    @DisplayName("detail에서 예약이 가능한 날짜를 가져오는 테스트")
    void getAbleDates() throws Exception {
        ConcertDetail detail1 = new ConcertDetail(
                1L,
                1L,
                LocalDateTime.of(2024, 8, 1, 19, 0),
                LocalDateTime.of(2024, 7, 1, 9, 0)
        );

        ConcertDetail detail2 = new ConcertDetail(
                2L,
                1L,
                LocalDateTime.of(2024, 8, 2, 19, 0),
                LocalDateTime.of(2024, 7, 1, 9, 0)
        );

        List<ConcertDetail> details = List.of(detail1, detail2);

        //given
        given(concertFacade.getAbleDates(anyLong())).willReturn(details);

        //when & then
        //Todo 나중에는 실제로 좌석이있는지 없는지 Facade에서 다른 서비스와의 관계를 통해 확인하고 가능한 날짜들을 반환해야함
        mockMvc.perform(get("/concert/reservation/days/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].concertId").value(1L))
                .andExpect(jsonPath("$[0].startDate").value("2024-08-01T19:00:00"))
                .andExpect(jsonPath("$[1].concertId").value(1L))
                .andExpect(jsonPath("$[1].startDate").value("2024-08-02T19:00:00"));
    }
}