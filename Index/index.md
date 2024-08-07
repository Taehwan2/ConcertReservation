### 인덱스 보고서

우선 콘서트 조회
한 공연장에서 콘서트는 많아봤자 10-20개라고 생각 되어서 데이터의 부하를 따로 생각하지 않았습니다.

#### 1. 콘서트의 옵션 ID 와 좌석 번호로 예약을 조회하는 쿼리.
``` select * from concert_seat where concert_detail_id = 12 and seat_no =44  ```

'좌석을 등록할' 때 그 좌석이 예약되어있는지, 혹은 생성만 되어있고 예약가능한 상태인지 확인하는 쿼리입니다.
데이터를 만건을 넣고 기본적으로 조회를 했더니 속도가 엉청 오래걸리는 것을 확인했습니다.
또한 많은 사람들이 좌석예약정보를 조회하기 때문에 인덱스가 필요할 것이라고 생각했습니다.

![10](https://github.com/user-attachments/assets/a3ae2fe2-af19-4516-88ad-adf655763d50)



![11](https://github.com/user-attachments/assets/fb2c215e-e438-4416-bab2-20abc3d192f5)

![12](https://github.com/user-attachments/assets/d2ef47bb-e810-4855-832a-82125afc83e3)


인덱스가 없을 때 explain 을 통해서 확인하면 index가 안타고 실행 시간이 109 ms 걸리는 것을 확인할 수 있었습니다.


여기서 문제가 생겼던점
원인 : 임시 데이터로 concert_detail_id =1로 10000건을 넣었더니 인덱스의 효율이 거의 없는 것을 판단
정확하게 이야기하면 index로 걸었던 concert_detail_id 가 모든 데이터가 1L이여서 인덱스 효율이 안생겼습니다.


concert_detail_id를 1 ~ 20까지 seat 50자리까지 해서 10479개의 데이터를 입력하고 수행했습니다.

![13](https://github.com/user-attachments/assets/08dc0480-05fa-46d0-9b87-de1874513fe3)
![14](https://github.com/user-attachments/assets/7b8b6698-8966-4bcb-ae11-51d0065637a2)


index설정한 것으로 타게 되고, 속도도 상당하게 빨라진 것을 알 수 있습니다.

여기서 고려해야 할 부분은 concert_detail_id 를 먼저 걸어야 하나 seat_no 를 먼저 걸어야 하나의 

문제가 있었습니다.

![15](https://github.com/user-attachments/assets/72f3b619-e2cb-42f8-a384-470da022c8a6)
![16](https://github.com/user-attachments/assets/4485d1aa-c73c-4bb1-b7c7-49b81d305c8c)

<인덱스 이름 오류.. idx_seatNo_concertDetail 로 해야하는데 미처 바꾸지 못했습니다.>

애초에 특정 콘서트에 대한 좌석을 서칭하는 게 맞고, 좌석은 모든 옵션에 동일하게 1-50으로 분포되어있어서 concert_detail_id로 필터링하고 좌석을 찾는게 좋다와
그리고 concert_detail_id를 단독으로해서 조회하는 쿼리도 있지만,

seatno를 단독으로해서 하는 쿼리가 없기때문에 위와같은  결론을 내렸습니다.
결론:
복합키 (concertDetailId, seatNo) 이 두컬럼으로 걸었습니다.


#### 2. 좌석 조회

``` select * from concert_seat where concert_detail_id = 12 and seat_status = 'TEMP' ```
예약 가능한 좌석을 조회하기 위해서
'예약된 상태가 아닌 모든 좌석을 불러오기 위한' 쿼리.
이 쿼리는 예약가능한 좌석이 뭐가있는지 확인할 때 쓰는 쿼리여서 많은 사람들이 사용하는 쿼리라고 판단했습니다.

인덱스를 걸지 않았을 떄 조회시에 많은 시간이 걸리는 것을 확인했습니다. 
![17](https://github.com/user-attachments/assets/113ab341-8171-4ae3-9e8b-f3d3733aeb73)
![18](https://github.com/user-attachments/assets/dedb58f6-7d42-4d6f-8586-33509158d919)
![19](https://github.com/user-attachments/assets/95a95299-a28a-4a64-bd9f-e218781e23e5)


좌석의 상태에 따른 조회 쿼리에 대해서 인덱스를 걸어보려고 했는데.. seat_status는 카디널리티가 높지 않기 때문에 concert_detail_id로 인덱스를 잡았고,
복합 인덱스로 (concert_detail_id, seat_status)로 잡았습니다.
![20](https://github.com/user-attachments/assets/c277ddbe-4b73-4a76-a9bc-209cc73356bb)
![21](https://github.com/user-attachments/assets/8119251a-b125-4fe7-aa1f-8c335851633b)


인덱스를 적용했을 때 쿼리 조회속도가 크게 상승하고,  필터가 많이 걸리는 것을 볼 수 있었습니다.


### 3. 결제 좌석 조회

결제를 할 시에 user가 어느 좌석을 예약해놨는지 List형태로 예약한 좌석을 조회하는 쿼리
``` select * from concert_seat where userId = ?  ```

인덱스가 없을 떄 만 ~ 10만 건의 데이터만 있어도 느려지는 것을 확인했습니다.

![25](https://github.com/user-attachments/assets/4104f5e4-114f-4ebe-b813-0bf37a2da83a)
![26](https://github.com/user-attachments/assets/8e0389e2-d578-44b8-853b-e0b85c8dd3ba)

그래서 userId에 인덱스를 걸고 쿼리를 분석해본 결과

![27](https://github.com/user-attachments/assets/532ca0eb-9e59-4bce-b1a9-9a461bbd7957)
![28](https://github.com/user-attachments/assets/daf7659b-8c50-4c49-8608-51de91bcb828)

속도나 이런 부분에서 큰 차이가 없어서 이것 저것 실행해보며 찾은 결과
데이터가 userId = 1 인 것만 만건 10만건이 들어가있어서 index의 효율성이 극히 떨어진 것을 확인했습니다. 그렇기에
userId를 1 ~ a 로 증가시키면서 데이터를 넣었고,

![29](https://github.com/user-attachments/assets/4039aefe-af1c-4746-9951-309f3b96647e)
![30](https://github.com/user-attachments/assets/2bab18e1-1fd7-4bcb-bc5d-4ec9e5311aba)

userId의 데이터 분포가 늘면 늘수록 속도도 줄고, 인덱스를 활용하여 필터를 하는 것을 볼 수 있었습니다.


### 4. 콘서트 detail에서 예약가능한 날짜 조회.
``` select * from concert_detail where concert_id = ? and now() < start_date and now() < reservation_start_date; ```

위에 index를 고른 이유와 동일하고, 좌석 예약 상태 조회, 예약 좌석 조회, 결제 좌석조회, 와 같이 콘서트 예약가능일은 사람들이 콘서트 예약을 할 때 가장많이 사용하는 api로
쿼리의 사용이 역시 많을 것이라고 생각했기 때문에 index 분석에 들어갔습니다.

![31](https://github.com/user-attachments/assets/1b2b7017-f767-45a2-9eb4-1d3e6d5e2205)

![32](https://github.com/user-attachments/assets/50a59371-8c6b-4b46-9e15-bea5f6ca8b32)


콘서트에 예약가능한 날짜를 찾는 쿼리를 인덱스를 걸려고 보았더니.. 범위를 가진 쿼리였습니다..

그래서 아무렇게나 데이터를 넣고 테스트를 해봤더니 인덱스를 타지 않는 불상사가 생겼습니다.


처음에는 찾아보니 
explain SELECT * FROM concert_detail c where c.concert_id =1  and  now() < c.start_date  and now()  < c.reservation_start_date;
쿼리에서 now() 함수를 쓰면 인덱스를 안탄다해서


SET @currentTime = NOW();

SELECT * FROM concert_detail c
WHERE c.concert_id = 1
AND @currentTime < c.start_date
AND @currentTime < c.reservation_start_date;
아래와 같이 바꾸고 실행했는데도 풀 스캔을 하는 것이었다.


이유: 
데이터를 for문을 통해서 concertId만 변환하면서 넣었고, 모두 start_date랑 reservation_start_date
의 범위조건을 통과하게 데이터를 넣었더니 인덱스를 그냥 타지 않아버린것이었다.
결과: start_date와 reservation_strat_date의 값을 바꿔가면서 데이터 분포를 늘렸더니 범위를 가진 쿼리인데도 인덱스를 타는 것을 볼 수 있었다!!
데이터 분포의 중요성..

index는 (concertId,start_date,reservation_start_date())를 통해서 걸었습니다.

![33](https://github.com/user-attachments/assets/23d75355-4072-49cc-acee-11107a2ab88e)
![34](https://github.com/user-attachments/assets/9e8c982f-a4d7-4611-87eb-0838b319cee9)

아래와 같이 index를 정상적으로 타고 데이터를 늘릴수록 효과가 있다는 것을 확인했습니다.