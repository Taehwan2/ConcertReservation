## 시나리오 부하테스트

### 시나리오 종류
#### 1.유저의 포인트 조회 및 충전/사용

유저는 콘서트를 예약하기 위해서 잔액을 조회하고, 충전 및 사용할 것이라고 생각했고 
이 시나리오 안에서는 평상시에 유저가 포인트를 조회하거나 충전하는 상황에서 서서히 서비스에 사람이 많아 졌다는 가정하에 
duration과 target을 늘리며 부하테스트를 하였다.

stages 의 duration과 taget을 변화시켜가며 docker 리소스 지표를 통해
cpu와 memory network의 지표를 살펴볼 예정.

```
import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages:[
        {duration: '1s', target: 1},
        {duration: '1',target: 1}
        ]
};

export default function(){
        const BASE_URL = `http://localhost:8080/point`;
        let userId = Math.floor(Math.random() * 899 +1)

        http.get(`${BASE_URL}/${userId}`);


        const postData = JSON.stringify({
                "userId": userId,
                "charge": 1000,
                "@class": "pointReq"

        });

        const params = {
                headers: {
                'Content-Type': 'application/json'
                }

        };

        const patchUrl = 'http://localhost:8080/point';
        http.patch(patchUrl,postData,params);


        sleep(1);


}

처음에 예상했던 TPS는 2번째 테스트를 통해서 부하를 적당히 준다는 가정하에 100TPS 정도 넘으면 잘나오겠다고 예상을 헀다.

```
![스크린샷 2024-08-21 오후 9 32 10](https://github.com/user-attachments/assets/79276a24-ee4b-4b44-9a1a-989460f34ca5)

![스크린샷 2024-08-21 오후 9 37 44](https://github.com/user-attachments/assets/2436e7e7-025a-44a3-a0f8-f932aafe21e7)

### 분석결과
1. 대기열과는 관련이 없어 데이터 요청 실패는 한건도 없었다 -> 데이터 무결성 보존
2. TPS를 조회한 결과 user의 양과 duration을 끝도없이 올리면 계속해서 TPS 가 올라갈 줄 알았지만, 7번째 테스트에서 8번째 테스트로 넘어갈 때 부터 오히려 CPU양이 힘들어하고 TPS도 주는 것을 보았다..
이것이 병목현상..?
3. DB는 오히려 널널한데 spring app의 cpu가 터져서 뻗기 때문에 .. 챕터 20 보완상황에 tomcat thread를 늘려서 테스트해보기로 했다.

처음 예상했던 TPS보다 좀더 높아서 spring app과 DB만 썼을 때 TPS가 잘나오는 것 같다고 생각했다.
-> 하지만 DB에 부하가 더욱 더 많이 걸리면 부하를 분산해야 TPS가 더 잘나온다는 것을 아래를 통해 확인했다.



#### 2. 콘서트와 콘서트 옵션 조회 및 대기열 등록

유저는 콘서트를 예약하기 위해서 원하는 콘서트와 콘서트 옵션에 대해서 조회를 할 것이고,
예약을 하기위해 대기열 등록을 실행할 것이라는 것을 예상하고 시나리오를 작성


이 시나리오 안에서는 평상시에 간단한 조회와 대기열에 등록을 하다가 서서히 이벤트를 통해서
부하가 늘었을 때를 가정

stages 의 duration과 taget을 변화시켜가며 docker 리소스 지표를 통해
cpu와 memory network의 지표를 살펴볼 예정.


```
import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages:[
        {duration: '5s', target: 50},
        {duration: '20s',target: 899}
        ]
};

export default function(){
        const BASE_URL = `http://localhost:8080/concert/1`;
        const BASE_URL2 = `http://localhost:8080/concerts/1`;
        let userId = Math.floor(Math.random() * 899 +1)

        http.get(BASE_URL);
        http.get(BASE_URL2);


        const postData = JSON.stringify({
                "userId": userId,
                "@class": "queueReq"
        });

        const params = {
                headers: {
                'Content-Type': 'application/json'
                }

        };

        const postUrl = 'http://localhost:8080/concert/wait';
        http.post(postUrl,postData,params);


        sleep(1);


}

```
1. 콘서트 조회
2. 콘서트 옵션 조회
3. 대기열 등록

![Uploading 스크린샷 2024-08-21 오후 10.08.57.png…]()

![스크린샷 2024-08-21 오후 10 09 16](https://github.com/user-attachments/assets/8888ec7c-4d4c-4b5c-97b5-9f807d2e5302)

처음 예상했던 TPS는 마찬가지로 2번 째 테스트를 보고 100정도 였다. 
확실 Redis를 사용하기 때문에 DB에 부하도 적고, redis도 부하가 크게 걸리는 것 같아 보이지는 않았다.

동일하게 무조건 부하를 늘린다고 TPS가 계속 상승하지않고 병목이걸린다.

결과: 
생각보다 redis의 리소스가 튼튼하다?? 특별히 부하가 많이안걸리고 레디스 덕분에 DB에도 부하가 안온다.
하지만 여전히 spring app의 cpu는 300%에 가깝다..







#### 3. 대기열 등록 후 콘서트 예약가능일 조회
 * 여기서부턴 대기열에 등록한 후 이기때문에 대기열 순번이 필요하다 
유저는 대기열에 등록할 것이고, 활성이 된다면 콘서트 예약가능일을 조회할 수 있다.


이 시나리오 안에서는 평상시에 활성 유저가 예약가능일을 조회하는 시나리오로 서서히 이벤트를 통해서
부하가 늘었을 때를 가정

stages 의 duration과 taget을 변화시켜가며 docker 리소스 지표를 통해
cpu와 memory network의 지표를 살펴볼 예정.

```
import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages:[
        {duration: '1s', target: 2},
        {duration: '1s',target: 899}
        ]
};

export default function(){

        let userId = Math.floor(Math.random() * 899 +1)

        const postData = JSON.stringify({
                "userId": userId,
                "@class": "queueReq"
        });

        const params = {
                headers: {
                'Content-Type': 'application/json'
                }

        };

        const postUrl = 'http://localhost:8080/concert/wait';
        http.post(postUrl,postData,params);



        sleep(30);


        const getUrl = `http://localhost:8080/concert/reservation/days/1`;

         const getParams = {
                 headers: {
                 'Authorization': userId.toString()
                 }
         };

  http.get(getUrl, getParams);

  sleep(1);
}







---------------------------------------------------------------- 중간 http 실패시 바로 테스트 종료


import http from 'k6/http';
import { sleep, check, fail } from 'k6';

export let options = {
  stages: [
    { duration: '1s', target: 2 },
    { duration: '1s', target: 899 }
  ]
};

export default function () {
  let userId = Math.floor(Math.random() * 899 + 1);

  const postData = JSON.stringify({
    "userId": userId,
    "@class": "queueReq"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  const postUrl = 'http://localhost:8080/concert/wait';
  let postRes = http.post(postUrl, postData, params);

  // Check if the response is 200
  if (!check(postRes, { 'POST /concert/wait is 200': (r) => r.status === 200 })) {
    fail('POST /concert/wait failed');
  }

  sleep(30);

  const getUrl = `http://localhost:8080/concert/reservation/days/1`;

  const getParams = {
    headers: {
      'Authorization': userId.toString()
    }
  };

  let getRes = http.get(getUrl, getParams);

  // Check if the response is 200
  if (!check(getRes, { 'GET /concert/reservation/days/1 is 200': (r) => r.status === 200 })) {
    fail('GET /concert/reservation/days/1 failed');
  }

  sleep(1);
}

```

![스크린샷 2024-08-21 오후 10 49 57](https://github.com/user-attachments/assets/f3706777-db7e-4267-96a2-6cdea7e680ad)

대기열 활성화를 실패한 인원은 다음과 같이 에러를 만나고 종료하게 된다.


![스크린샷 2024-08-21 오후 11 18 44](https://github.com/user-attachments/assets/4b5b9a32-0a59-4cd4-a7e2-44a24a6cb5ff)

![스크린샷 2024-08-21 오후 11 19 07](https://github.com/user-attachments/assets/5de59701-98ba-421e-94dd-5b89c6c71398)







#### 4. 대기열 등록 후  예약가능한 좌석을 조회하고, 좌석을 예약하는 시나리오(대기열 활성화했다고 가정)
콘서트 예약일을 조회한 유저는 콘서트를 예약하기 위해서 좌석을 조회하고 예약할 것이다.

이 시나리오 안에서는 평상시에 활성 유저가 좌석을 조회하고 예약하는 시나리오로 서서히 이벤트를 통해서
부하가 늘었을 때를 가정

stages 의 duration과 taget을 변화시켜가며 docker 리소스 지표를 통해
cpu와 memory network의 지표를 살펴볼 예정.

```


import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages:[
        {duration: '1s', target: 1},
        {duration: '1s',target: 1}
        ]
};

export default function(){

        let userId = Math.floor(Math.random() * 899 +1)
        let seatId = Math.floor(Math.random() * 50 + 1)
        const postData = JSON.stringify({
                "userId": userId,
                "@class": "queueReq"
        });

        const params = {
                headers: {
                'Content-Type': 'application/json'
                }

        };

        const postUrl = 'http://localhost:8080/concert/wait';
        http.post(postUrl,postData,params);

        sleep(20);

         const getSeatUrl = 'http://localhost:8080/concert/seats/1';


         const getParams = {
                 headers: {
                 'Authorization': userId.toString()
                }
         };

        http.get(getSeatUrl, getParams);
        sleep(1);

        const getUrl = `http://localhost:8080/concert/seat`;


        sleep(1);


         const patchData = JSON.stringify({
         "@class" : "concertSeat",
          "userId" : userId,
          "concertDetailId" : 1,
          "seatNo" : seatId
        });

        const params3 = {
                headers: {
                 'Authorization': userId.toString(),
                'Content-Type': 'application/json'
                }
        };

        http.patch(getUrl,patchData,params3);



          sleep(1);

}



---------------------------------------------------------------- 중간 http 실패시 바로 테스트 종료

import http from 'k6/http';
import { sleep, check, fail } from 'k6';

export let options = {
  stages: [
    { duration: '1s', target: 1 },
    { duration: '1s', target: 1 }
  ]
};

export default function () {
  let userId = Math.floor(Math.random() * 899 + 1);
  let seatId = Math.floor(Math.random() * 50 + 1);

  const postData = JSON.stringify({
    "userId": userId,
    "@class": "queueReq"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  const postUrl = 'http://localhost:8080/concert/wait';
  let postRes = http.post(postUrl, postData, params);

  // Check if the response is 200
  if (!check(postRes, { 'POST /concert/wait is 200': (r) => r.status === 200 })) {
    fail('POST /concert/wait failed');
  }

  sleep(20);

  const getSeatUrl = 'http://localhost:8080/concert/seats/1';

  const getParams = {
    headers: {
      'Authorization': userId.toString()
    }
  };

  let getSeatRes = http.get(getSeatUrl, getParams);

  // Check if the response is 200
  if (!check(getSeatRes, { 'GET /concert/seats/1 is 200': (r) => r.status === 200 })) {
    fail('GET /concert/seats/1 failed');
  }

  sleep(1);

  const getUrl = `http://localhost:8080/concert/seat`;

  sleep(1);

  const patchData = JSON.stringify({
    "@class": "concertSeat",
    "userId": userId,
    "concertDetailId": 1,
    "seatNo": seatId
  });

  const params3 = {
    headers: {
      'Authorization': userId.toString(),
      'Content-Type': 'application/json'
    }
  };

  let patchRes = http.patch(getUrl, patchData, params3);

  // Check if the response is 200
  if (!check(patchRes, { 'PATCH /concert/seat is 200': (r) => r.status === 200 })) {
    fail('PATCH /concert/seat failed');
  }

  sleep(1);
}

``` 
예상했던 TPS는 이부분도 다른 부분과 같이 데이터를 분산 시켰기 떄문에 TPS가 높게 나올 줄 알아서 150정도를 예상했다
하지만,, 도커의 리소스보다는 지금 나의 컴퓨터의 리소스가 버티지 못하는지 spring app의 cpu 부하가 많이가니 테스트도 못하고 꺼져버렸다..

![스크린샷 2024-08-21 오후 11 51 35](https://github.com/user-attachments/assets/5daf4e41-684d-4a42-870c-1a7d6bc89ba4)
<DB예약 내역>
![스크린샷 2024-08-21 오후 11 55 15](https://github.com/user-attachments/assets/a2ecf997-578d-408f-a6ec-61d8038f2412)
<결과 지표>

![스크린샷 2024-08-21 오후 11 57 16](https://github.com/user-attachments/assets/0ef6a262-80e7-445d-bc98-0f372d224d1c)
<성공사례>
DB를 보면 사용자 별로 50번 까지 예약이 되는 것을 확인 할 수 있고, 중복예약이 없는 것도 확인했다 
다만 CPU가 400%이상이 가면서 더이상의 부하를 견디지 못하고 계속 중단된다.. 이게 병목 현상이다
하지만 지표를 보면 아직 redis 자원도 부담없고 DB 자원도 부담이 없다는 것을 확인할 수 있다.
spirng app의 CPU를 해결해야할 것 같다.



#### 5. 대기열 등록 후  좌석을 예약한 사람은 예매 후 결제 및 예약을 하는 시나리오(대기열 활성화했다고 가정)
좌석을 예약한 사람은  좌석 예매 후 결제 및 예약을 할것이다.

이 시나리오 안에서는 평상시에 활성 유저가  좌석 예매 후 결제 및 예약을 하는 시나리오로 서서히 이벤트를 통해서
부하가 늘었을 때를 가정

stages 의 duration과 taget을 변화시켜가며 docker 리소스 지표를 통해
cpu와 memory network의 지표를 살펴볼 예정.


```
import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages:[
        {duration: '2s', target: 1},
        {duration: '2s',target: 1}
        ]
};

export default function(){

        let userId = Math.floor(Math.random() * 899 +1);
        let seatId = Math.floor(Math.random() * 50 + 1);

        const postData = JSON.stringify({
                "userId": userId,
                "@class": "queueReq"
        });

        const params = {
                headers: {
                'Content-Type': 'application/json'
                }

         };

        const postUrl = 'http://localhost:8080/concert/wait';
        http.post(postUrl,postData,params);

        sleep(20);


        const getUrl = `http://localhost:8080/concert/seat`;

        const patchData = JSON.stringify({
        "@class" : "concertSeat",
        "userId" : userId,
        "concertDetailId": 1,
        "seatNo": seatId

         });

          const params3 = {
                headers: {
                 'Authorization': userId.toString(),
                'Content-Type': 'application/json'
                }
        };



        http.patch(getUrl,patchData,params3);

         sleep(1);

    const getUrl2 = 'http://localhost:8080/concert/reservation/days';

           const reserveData = JSON.stringify({
                  "@class" : "concertSeat",
                  "userId" : userId,
                  "concertDetailId": 1,
                  "seatNo": 3
                 });

        const postParams = {
                 headers: {
                 'Authorization': userId.toString(),
                 'Content-Type': 'application/json'
                }
         };

  http.post(getUrl2,reserveData ,postParams);

   sleep(1);
}
---------------------------------------------------------------- 중간 http 실패시 바로 테스트 종료
import http from 'k6/http';
import { sleep, check, fail } from 'k6';

export let options = {
  stages: [
    { duration: '2s', target: 1 },
    { duration: '2s', target: 1 }
  ]
};

export default function () {
  let userId = Math.floor(Math.random() * 899 + 1);
  let seatId = Math.floor(Math.random() * 50 + 1);

  const postData = JSON.stringify({
    "userId": userId,
    "@class": "queueReq"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  const postUrl = 'http://localhost:8080/concert/wait';
  let postRes = http.post(postUrl, postData, params);

  // Check if the response is 200
  if (!check(postRes, { 'POST /concert/wait is 200': (r) => r.status === 200 })) {
    fail('POST /concert/wait failed');
  }

  sleep(20);

  const getUrl = `http://localhost:8080/concert/seat`;

  const patchData = JSON.stringify({
    "@class": "concertSeat",
    "userId": userId,
    "concertDetailId": 1,
    "seatNo": seatId
  });

  const params3 = {
    headers: {
      'Authorization': userId.toString(),
      'Content-Type': 'application/json'
    }
  };

  let patchRes = http.patch(getUrl, patchData, params3);

  // Check if the response is 200
  if (!check(patchRes, { 'PATCH /concert/seat is 200': (r) => r.status === 200 })) {
    fail('PATCH /concert/seat failed');
  }

  sleep(1);

  const getUrl2 = 'http://localhost:8080/concert/reservation/days';

  const reserveData = JSON.stringify({
    "@class": "concertSeat",
    "userId": userId,
    "concertDetailId": 1,
    "seatNo": 3
  });

  const postParams = {
    headers: {
      'Authorization': userId.toString(),
      'Content-Type': 'application/json'
    }
  };

  let postRes2 = http.post(getUrl2, reserveData, postParams);

  // Check if the response is 200
  if (!check(postRes2, { 'POST /concert/reservation/days is 200': (r) => r.status === 200 })) {
    fail('POST /concert/reservation/days failed');
  }

  sleep(1);
}

```
예상했던 TPS는 이부분도 다른 부분과 같이 데이터를 분산 시켰기 떄문에 TPS가 높게 나올 줄 알아서 150정도를 예상했다
하지만,, 도커의 리소스보다는 지금 나의 컴퓨터의 리소스가 버티지 못하는지 spring app의 cpu 부하가 많이가니 테스트도 못하고 꺼져버렸다..

![스크린샷 2024-08-22 오전 12 06 16](https://github.com/user-attachments/assets/4cc37084-5583-4397-8edb-3affc1b8b57c)
1명으로 테스트했을 떄는 정상 작동 하는 것을 볼 수 있다.

![스크린샷 2024-08-22 오전 12 19 35](https://github.com/user-attachments/assets/677f28f4-d913-45c7-b400-a3ca7f0763c2)

현재 내 컴퓨터의 자원과 도커 자원으로 인해 적은 부하에도 서버가 죽는 것으로 파악할 수 있었다.
도커의 CPU가 400%가 넘어가면 바로 꺼진다..
하지만 부하테스트를 통해서 spinrg app의 cpu가 데이터 처리에 큰 영향을 미친다는 것을 알 수 있고,
평소에 적은 부하로 테스트 할 때와 비교하여 점점 더 부하를 세게 주었을 때 
어느 기술을 사용해서 어떤 자원으로 부하가 분산되는 지를 확인했고, 분명 부하를 늘릴 수 록 TPS가 정체되는 병목현상이 일어나는 지점이 있는 것을 확인했다.
또한 부하테스트의 결과 통해서 내 로직이 정상적으로 작동할 수 있다는 것을 알게되었다.



![스크린샷 2024-08-22 오전 12 32 41](https://github.com/user-attachments/assets/c12d7415-1882-4c04-b5ef-797d796ada28)
데이터는 무결성을 지키며 들어가서 예약정보를 나타내는 것을 알 수 있다.


docker container 리소스 지표를 보며 부하테스트를 완료
![스크린샷 2024-08-22 오전 12 33 06](https://github.com/user-attachments/assets/fbff52c6-bba0-4b00-85e8-0f6075b619af)