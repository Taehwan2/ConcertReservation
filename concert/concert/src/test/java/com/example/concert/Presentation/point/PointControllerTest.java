package com.example.concert.Presentation.point;

import com.example.concert.Application.UserPointFacade;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPointFacade userPointFacade;

    @Test
    @DisplayName("유저아이디 1인 태환이가 가진 포인트를 조회하는 로직")
    public void testLookupPointTest() throws Exception {
        User point = new User(1L, "태환", new BigDecimal("100.00"));

        //given
        given(userPointFacade.getUserPoint(1L)).willReturn(point);

        //TODO 100이 나와야한다.
        //when & then
        mockMvc.perform(get("/point/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.point").value("100.0"));
    }

    @Test
    @DisplayName("유저아이디 2인 태환이가 CHARGE 포인트를 충전하는 로직검증")
    public void testChangePoint() throws Exception {
        PointHistory pointHistory = new PointHistory(1L, 2L, new BigDecimal("50.00"), PointType.CHARGE);
        PointRequest pointRequest = new PointRequest(2L, new BigDecimal("50.00"));

        //TODO 50원이 저장되었다고 나와야한다.
        //given
        given(userPointFacade.changePoint(any(PointRequest.class))).willReturn(pointHistory);


        //when & then
        mockMvc.perform(patch("/point")
                        .contentType(APPLICATION_JSON)
                        .content("{\"userId\": 2, \"charge\": \"50.00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.pointType").value("CHARGE"));
    }
}