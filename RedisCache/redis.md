## 1. cache
캐시를 사용하는 경우는 자주 사용하지만 업데이트가 적을 때 사용한다.
DB를 사용하는 비용보다 redis를 사용하는 비용이 더 좋고, 빠르기 때문.

콘서트 서비스에서 캐시를 사용할만한 경우는 3가지이다.

# 콘서트 조회
로직: 콘서트를 조회했을 때 콘서트 정보는 업데이트 되거나 삭제되지않는 이상 바뀌지 않기 떄문에
처음조회시 DB에서 가져오고 가끔 update될 때 캐시를 업데이트해준다. 그리고 데이터가 삭제되면 캐시에서 삭제해버리는 로직을 사용했다.

<img width="1391" alt="스크린샷 2024-08-01 오후 10 06 08" src="https://github.com/user-attachments/assets/1d572e03-04e0-430d-94ab-8a9092de6806">
처음 콘서트를 조회했을 때 

![스크린샷 2024-08-01 오후 10 04 06](https://github.com/user-attachments/assets/0c46e58f-8fe3-4775-b9bb-aeb0a957867c)
sql qurey문과 함께 나가는 것을 확인할 수 있다.

하지만 첫번째 조회시 cache에 값을 저장했고, 계속 hit를 하면서 query를 날리지 않는 것을 확인 할 수 있다.
![스크린샷 2024-08-01 오후 10 05 13](https://github.com/user-attachments/assets/c5e2cf4c-ad0f-4422-b5b0-8cddf5dcb4db)


아래 사진을 참고하면 redis에 잘 저장된 것을 확인할 수 있다.
<img width="862" alt="스크린샷 2024-08-01 오후 10 05 25" src="https://github.com/user-attachments/assets/cea29858-dc2b-40ff-9b7c-8c9a5ebaf3de">

<img width="1383" alt="스크린샷 2024-08-01 오후 10 06 03" src="https://github.com/user-attachments/assets/073c2ec3-890d-4564-918f-2f6ab07b31e2">

이제 업데이트를 하면 CachePut이 되어있기 때문에 캐시를 업데이트한다.
![스크린샷 2024-08-01 오후 10 05 58](https://github.com/user-attachments/assets/f34d349f-d72f-4680-b7df-5022dd5259c7)


아래를 보면 캐시도 업데이트가 된것을 볼 수 있다.
<img width="666" alt="스크린샷 2024-08-01 오후 10 06 17" src="https://github.com/user-attachments/assets/a55e191b-7700-4d28-a17a-3182481ec789">


마지막으로 데이터를 삭제하면
<img width="1371" alt="스크린샷 2024-08-01 오후 10 06 27" src="https://github.com/user-attachments/assets/ef370d5c-7a01-4bcf-a20a-2fdf7a658abe">


삭제 쿼리가 나가게되고,
![스크린샷 2024-08-01 오후 10 06 31](https://github.com/user-attachments/assets/ec7bafa8-c5a0-47e6-ab54-b2d6d275d792)

redis에 있는 캐시가 삭제되게 된다.
<img width="838" alt="스크린샷 2024-08-01 오후 10 06 38" src="https://github.com/user-attachments/assets/e3999f38-b381-4416-a035-3c94630af2db">



# 콘서트 옵션 조회

로직: 콘서트 옵션을 조회했을 때 콘서트 옵션 정보는 업데이트 되거나 삭제되지않는 이상 바뀌지 않기 떄문에
처음조회시 DB에서 가져오고 가끔 update될 때 캐시를 업데이트해준다. 그리고 데이터가 삭제되면 캐시에서 삭제해버리는 로직을 사용했다.

<img width="1367" alt="스크린샷 2024-08-01 오후 10 06 59" src="https://github.com/user-attachments/assets/453d9490-512e-4b8e-9189-f63e3ca9b5d4">

콘서트 옵션을 조회하게 되면

![스크린샷 2024-08-01 오후 10 06 54](https://github.com/user-attachments/assets/7e1c079d-ba59-483f-a498-b7c664b4507d)

콘서트 옵션 조회 query가 나가게되고
캐시에 저장되게 된다.
<img width="826" alt="스크린샷 2024-08-01 오후 10 07 39 복사본" src="https://github.com/user-attachments/assets/a718f978-be60-48cc-a668-71b59e1c129a">



두번째 부터는 
![스크린샷 2024-08-01 오후 10 07 09](https://github.com/user-attachments/assets/ed7da352-71e9-44db-b65f-2fb9c9eaff82)

쿼리가 안나가고 redis에서 불러온다.


<img width="1383" alt="스크린샷 2024-08-01 오후 10 30 13" src="https://github.com/user-attachments/assets/fdb272bd-ab06-4b16-a8d9-72c9bee8a6d3">

만약 update를 한다면
![스크린샷 2024-08-01 오후 10 34 28](https://github.com/user-attachments/assets/4a0cd2a0-0cc8-44ca-a815-d14dd0e15025)

update query가 나가고
![Uploading 스크린샷 2024-08-01 오후 10.34.28.png…]()

<img width="849" alt="스크린샷 2024-08-01 오후 10 34 03" src="https://github.com/user-attachments/assets/2a7dd128-35b5-4e00-a367-11c63be709fe">

캐시가 업데이트 되게된다.

만약 삭제를 하게되면 
<img width="1404" alt="스크린샷 2024-08-01 오후 10 07 44" src="https://github.com/user-attachments/assets/e007e8e3-23e5-4236-84b9-61b7f9657e9c">


delete query가 나오게되고
![스크린샷 2024-08-01 오후 10 07 51](https://github.com/user-attachments/assets/42fd55f8-5c9a-4dc0-b55f-962f6a7900fd)



캐시가 삭제되게 된다.
<img width="835" alt="스크린샷 2024-08-01 오후 10 08 15" src="https://github.com/user-attachments/assets/150539dc-4392-489a-aedd-e0225c65eeef">


# 좌석예약 조회
하지만 좌석예약은 수시로 업데이트 되기 떄문에 캐시에 부적절 하다고 판단 되어 캐시 플로우에서 제외를 시켰다.
