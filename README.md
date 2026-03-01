# seat-reservation-api

티켓팅(좌석 예약) 시스템을 가정한 백엔드 프로젝트입니다.  
동일 좌석 중복 예약 방지와 트랜잭션 적용을 중심으로 구현했습니다.

---

## 📌 프로젝트 목표

- 동일 좌석 중복 예약 방지
- 트랜잭션 적용을 통한 데이터 일관성 유지

---

## 🛠 기술 스택

- Java
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Gradle

---

## 🏗 시스템 구조

Controller → Service → Repository → Database

- Controller: HTTP 요청/응답 처리
- Service: 비즈니스 로직 및 트랜잭션 적용
- Repository: DB 접근
- Database: MySQL

---

## 📂 주요 기능

### 이벤트 / 섹션 / 좌석 생성
- 이벤트 생성
- 섹션 및 좌석 생성

### 좌석 조회
- 특정 섹션의 좌석 목록 조회

### 예약 생성
- 예약 요청 시 상태는 PENDING_PAYMENT
- 동일 좌석 중복 예약 방지
- 중복 발생 시 409 Conflict 반환

---

## 🔒 데이터 무결성 설계

DB 레벨 Unique 제약 적용

- 섹션: (eventId, name) 복합 유니크
- 좌석: (sectionId, seatNo) 복합 유니크
- 예약: seatId 유니크 제약
