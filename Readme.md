# 👁️‍🗨️ 개요

회원가입 및 로그인에 필요한 로직을 처리하는 서버입니다. 

<br>

# 🌆 기능 및 아키텍처


<details>
<summary> 📝 API 명세서 </summary>

<div markdown="1">
<br>

- [signup](#signup)
- [checkDuplicateMember](#checkDuplicateMember)
- [login](#login)
- [checkValidation](#checkValidation)
- [getMemberInfo](#getMemberInfo)

<br>

## signup

회원가입 요청을 처리합니다. 

<br>

### URL

- POST `/member/signup`

<br>


### 요청 예시

```json
{
    "email": "go123@gmail.com" ,
    "name" : "노희재",
    "password": "Testing" ,
    "birth": "1997-08-30" ,
    "phone" : "010-1234-5678" ,
    "gender" : "남"
}
```
<br>



### 응답 예시

- ✅ 성공

    ```json
    {
        "code": 200,
        "description": "성공"
    }
    ```
<br>

- ⚠️ 이미 존재하는 회원인 경우
    ```json
    {
        "code": 409,
        "description": "이미 존재하는 회원입니다"
    }
    ```
<br>

---
<br>

## checkDuplicateMember

이메일 중복체크를 합니다.

중복체크를 통과한다면, 이메일에 인증번호를 전송합니다. 

<br>

### URL

- POST `/member/checkDuplicateEmail`

<br>

### 요청 예시

```json
{
    "email": "go123@gmail.com"
}
```

<br>


### 응답 예시

- ✅ 성공

    ```json
    {
        "code": 200,
        "description": "성공"
    }
    ```

<br>


- ⚠️ 중복된 이메일인 경우
    ```json
    {
        "code": 409,
        "description": "중복된 이메일 입니다"
    }
    ```

<br>

---
<br>

## login
로그인 요청을 처리합니다. 

<br>

### URL

- POST `/member/login`
- Parameter
  - email: string
  - password: string

<br>

### 요청 예시

```json
{
    "email": "go123@gmail.com" ,
    "password": "Testing" ,
}
```
<br>

### 응답 예시

- ✅ 성공

    - header
        ```json
        {
            "authorization": ...
        }
        ```
        
    - Body
        ```json
        {
            "code": 200,
            "description": "성공"
        }
        ```

<br>

- 실패
    - ⚠️ 존재하지않는 회원정보일 경우
        ```json
        {
            "code": 404,
            "description": "존재하지 않는 회원입니다"
        }
        ```

<br>

---
<br>

## checkValidation

올바른 인증번호인지 확인합니다. 

<br>

### URL

- POST `/member/validation`
- Parameter
  - email: string
  - authNum: 

<br>


### 요청 예시

```json
{
    "email": "go123@gmail.com" ,
    "authNum": 123456
}
```

<br>


### 응답 예시

- ✅ 성공

    ```json
    {
        "code": 200,
        "description": "성공"
    }
    ```

<br>

- 실패
    - ⚠️ 잘못된 인증번호
        ```json
        {
            "code": 404,
            "description": "잘못된 인증번호입니다"
        }
        ```

<br>

---
<br>

## getMemberInfo

JWT 토큰으로 회원을 식별하고 회원의 정보를 가져옵니다.

<br>

### URL

- GET `/member/info`
- Headers
  - Authorization: login token

<br>


### 요청 예시

```json
{
    "email": "go123@gmail.com" ,
    "name" : "노희재",
    "password": "Testing" ,
    "birth": "1997-08-30" ,
    "phone" : "010-1234-5678" ,
    "gender" : "남"
}
```

<br>


### 응답 예시

```json
{
    "code": 200,
    "data": {
        "name": "노희재",
        "email": "go123@gmail.com",
        "phone": "010-1234-5678",
        "birth": "1997-08-30"
    },
    "description": "성공"
}
```
</div>
</details>

<br>

- 이메일 인증 절차

![이메일 인증 절차](https://user-images.githubusercontent.com/80504636/231195742-634eaef7-d77e-4e1c-a9d1-ffdc21f532e0.png)

1. **checkDuplicateMember** 로 요청이 오면, 백엔드 서버가 이메일 중복체크 로직을 거친 뒤 인증번호를 생성하며 이를 Redis 에 저장합니다. (만료시간 - 5분)

2. 생성된 인증번호를 RabbitMQ 에 전송요청(메세지)을 보냅니다.

3. RabbitMQ 는 Queue 에 메세지를 쌓아두고 순차적으로 Python 에  메일 전송요청을 보내게 됩니다.

4. Python 은 사용자에게 인증번호를 전송합니다.

5. 사용자는 인증번호를 입력하고, **checkValidation** 요청을 보냅니다.

6. Spring 은 사용자가 입력한 인증번호와 Redis 에 저장되어있는 인증번호를 비교하여 일치한다면 Redis 에 저장되어있는 값을 삭제하고 인증 성공 응답을 보냅니다. 


<br>

# 💽 ERD

![ERD](https://user-images.githubusercontent.com/80248626/231385844-44efb68f-9c16-49fb-a2af-ed0298982d5d.png)

<br>

# 🪵 개발환경

- Java 11

- Spring Boot 2.X
    - Spring Cloud Open Feign
    
    - Spring Data JPA
    
    - Spring Data Redis
    
    - Spring AMPQ

- MariaDB : 10.3

- Redis : 7.0.10

- RabbitMQ:3.11.10


<br>

# 💬 회고

### 프로젝트 진행시 주안점

- 다양한 로그인 처리 방법을 비교해 보고 프로젝트에 적합한 방법을 선택하고자 하였습니다.

- Redis, RabbitMQ 등 처음 써보는 소프트웨어를 적절한 사용처에 사용할 수 있도록 하였습니다.

<br>

### 한계점 및 개선 사항

- 초기에는 Refresh Token + Access Token 방식을 사용하여 인증, 인가 처리를 하였지만 Istio 에서 Refresh 토큰을 처리할 방법이 마땅치 않고, 처리할 방법들이 너무나 비효율적인 방법들이라고 생각하여 Access 토큰만을 이용한 인증,인가를 하였다는 점이 아쉽습니다.

- 예외처리, 테스트 코드 작성 등 미숙한 부분이 보여 아쉽습니다. 이 부분을 조금 더 보완하여 리팩토링을 진행하면 좋을것 같습니다.