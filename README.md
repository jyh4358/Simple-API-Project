## Eigene Korea - Backend Software Engineer Coding Test

<br/>
<br/>

### 환경
- Java 11
- Spring Boot 2.7.2
- Gradle 7.5
- MySQL 8.0.27

### 사용 기술
- Spring Data JPA
- Spring REST Docs
- Querydsl

### 패키지 구조

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%ED%8C%A8%ED%82%A4%EC%A7%80%EA%B5%AC%EC%A1%B0.JPG)


### E-R 다이어그램

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/er.JPG)

### REST Docs

현재 해당 과제를 EC2에 배포해놨습니다. 아래 링크에서 REST Docs를 확인하거나 로컬에서 jar 파일 실행하여 확인하실 수 있습니다.

[배포 REST Docs 링크](http://ec2-52-79-227-202.ap-northeast-2.compute.amazonaws.com:8080/docs/index.html)

[로컬 REST Docs 링크](http://localhost:8080/docs/index.html)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EC%8A%A4%ED%8A%B8%EB%8B%A5%EC%8A%A4.JPG)

<br/>

사용 가이드

- Item : 상품
  - GET: /items/{id}
    - id : 상품 식별자
    - 상품 상세 정보 조회
  - GET: /items
    - 전체 상품 상세 정보 조회
  - POST: /items
    - 상품 등록
  - PUT: /items/{id}
    - id : 상품 식별자
    - 상품 수정
  - DELETE: /items/{id}
    - id : 상품 식별자
    - 상품 삭제

- RecommendItem : 추천 상품
  - GET: /target-items/recommend-items?id=1,2
    - id : 선택한 상품들의 식별자 값들(콤마 ',' 로 구분), ex) 1,2,3
    - 선택한 상품들의 각각 추천 상품 조회
  - POST: /target-items/{targetId}/recommend-items
    - targetId : 추천 상품들을 등록할 상품의 식별자
    - 특정 상품의 추천 상품들을 등록
  - PUT: /target-items/{targetId}/recommend-items
    - targetId : 추천 상품들을 수정할 상품의 식별자
    - 특정 상품의 추천 상품들을 재등록
    - 기존 RecommendItem 테이블에 저장된 값들을 삭제 후 다시 저장
  - DELETE: /target-items/{targetId}/recommend-items/{recommendId}
    - targetId : 특정 추천 상품을 삭제할 상품의 식별자
    - recommendId : 추천 상품의 식별자


### 고려 사항 && 참고 사항

- private repository로 생성하였기 때문에 gitignore 처리를 하지 않았습니다.
- 해당 연관 상품코드의 연관도 순위(rank)는 DB에 따로 저장할 필요성을 못느껴 DB에 저장하지 않고 서비스단에서 처리하였습니다.
- csv 파일을 DB에 저장하는 로직을 구현하지 못했습니다. 때문에 AWS EC2에 배포하여 해당 데이터 값들을 넣어놨습니다.
  - 새로 저장하였기 때문에 csv에 저장된 기본키 값과는 다릅니다. 따라서 상품 전체 조회(GET: /items)하여 식별자를 확인한 이후에 API 요청을 해주시길 바랍니다.
  - 아래 링크에서 구현한 서비스를 이용하실 수 있습니다.
  - [배포 링크](http://ec2-52-79-227-202.ap-northeast-2.compute.amazonaws.com:8080)


### 빌드 방법

- ./gradlew build
- 생성된 jar 파일 실행
  1. MySQL (ddl-auto: create)
     - java -jar "-Dspring.profiles.active=local" .\build\libs\codingtest-0.0.1-SNAPSHOT.jar
     - DB 명: codingtest
     - url: jdbc:mysql://localhost:3306/codingtest
     - username: codingtest
     - password: codingtest
     
  2. MySQL (ddl-auto: update)
     - java -jar "-Dspring.profiles.active=dev" .\build\libs\codingtest-0.0.1-SNAPSHOT.jar
     - DB 명: codingtest
     - username: codingtest
     - password: codingtest
  3. H2 (ddl-auto: create)
     - DB 명: test
     - jdbc-url: jdbc:h2:tcp://localhost/./test
     - username: sa
     - password: