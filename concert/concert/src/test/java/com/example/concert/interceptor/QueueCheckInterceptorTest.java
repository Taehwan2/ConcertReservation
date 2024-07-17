package com.example.concert.interceptor;

import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import com.example.concert.domain.queue.service.QueueService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QueueCheckInterceptorTest {

    @Mock
    private QueueService queueService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handle;

    @InjectMocks
    private QueueCheckInterceptor queueCheckInterceptor;

    @Test
    @DisplayName("대기열에 아예없는 아이디를 입력하고, QUEUE NOT FOUND Expception return")
    void preHandle() throws Exception {
     //given
        given(request.getHeader("Authorization")).willReturn(null);

    //when
        BusinessBaseException e = (BusinessBaseException) assertThrows(BusinessBaseException.class,()->{
                queueCheckInterceptor.preHandle(request,response,handle);}
     );
     //then
     assertThat(e.getErrorCode()).isEqualTo(ErrorCode.QUEUE_NOT_FOUND);


    }

    @Test
    @DisplayName("대기열에 있지만 Working이 아니여서  QUEUE is Not Working")
    void preHandle2() throws Exception {
        //given
        given(request.getHeader("Authorization")).willReturn("1");
        given(queueService.isWorking(1L)).willReturn(false);
        //when
        BusinessBaseException e = (BusinessBaseException) assertThrows(BusinessBaseException.class,()->{
            queueCheckInterceptor.preHandle(request,response,handle);}
        );
        //then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.QUEUE_NOT_WORKING);


    }

    @Test
    @DisplayName("Working 중이여서 정상적인 처리를 하는 경우")
    void preHandle3() throws Exception {
        //given
        given(request.getHeader("Authorization")).willReturn("1");
        given(queueService.isWorking(1L)).willReturn(true);

        //when
      boolean  result = queueCheckInterceptor.preHandle(request,response,handle);

        //then
      assertThat(result).isEqualTo(true);

    }
}