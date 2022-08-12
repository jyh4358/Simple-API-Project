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

아래 링크에서 REST Docs를 확인하거나 로컬에서 jar 파일 실행하여 확인하실 수 있습니다.

[배포 REST Docs 링크](http://ec2-13-209-19-34.ap-northeast-2.compute.amazonaws.com:8082/docs/index.html)

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

  