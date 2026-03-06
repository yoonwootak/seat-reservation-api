# seat-reservation-api

티켓팅(좌석 예약) 시스템을 가정한 백엔드 프로젝트입니다.  
좌석 선점, 확정, 만료 처리와 동시성 제어를 중심으로 구현했습니다.

---

## 프로젝트 목표

- 동일 좌석에 대한 중복 선점 방지
- 좌석 선점 후 일정 시간 동안 결제 기회 제공
- 만료된 선점 자동 해제
- 동시 요청 환경에서의 데이터 무결성 보장

---

## 기술 스택

Java  
Spring Boot  
Spring Data JPA (Hibernate)  
MySQL  
Gradle

---

## 시스템 구조

Controller → Service → Repository → Database

Controller  
HTTP 요청과 응답을 처리

Service  
비즈니스 로직 수행 및 트랜잭션 경계 설정

Repository  
DB 접근

Database  
MySQL

---

## 주요 기능

### 좌석 조회

특정 섹션의 좌석 목록 조회  
좌석 상태(AVAILABLE / HELD / SOLD) 반환

---

### 좌석 선점

좌석 선택 시 HELD 상태로 변경  
holdToken 발급  
holdExpiresAt에 만료 시간 저장

---

### 좌석 확정

holdToken 검증 후 SOLD 상태로 변경  
선점한 사용자만 확정 가능

---

### 좌석 해제

결제 실패 또는 사용자 취소 시 HELD 상태 해제  
AVAILABLE 상태로 복구

---

### 선점 만료 처리

선점 시간이 지나면 자동 해제  
조회 시점과 스케줄러를 통해 만료 좌석 복구

---

## 좌석 상태 전이

AVAILABLE → HELD (좌석 선점)

HELD → SOLD (결제 성공 후 확정)

HELD → AVAILABLE (결제 실패 또는 사용자 취소)

HELD → AVAILABLE (선점 만료)

---

## 동시성 제어

초기에는 좌석을 조회한 뒤 자바 코드에서 상태를 검사하고 업데이트하는 방식으로 구현했습니다.

```
findById()
→ seat.hold()
→ 트랜잭션 종료 시 update
```

이 방식은 동시 요청 상황에서 여러 요청이 동시에 AVAILABLE 상태를 읽을 수 있어 중복 선점이 발생할 수 있었습니다.

이를 해결하기 위해 좌석 선점 로직을 **DB 조건부 UPDATE 방식**으로 변경했습니다.

핵심 아이디어

- `status = AVAILABLE` 인 경우 선점 가능
- `status = HELD` 이더라도 만료(`holdExpiresAt < now`)된 경우 선점 가능
- 그 외 상태는 선점 불가 → 409 Conflict

개념 예시

```sql
UPDATE seats
SET status = 'HELD',
    hold_token = ?,
    hold_expires_at = ?
WHERE id = ?
  AND (
       status = 'AVAILABLE'
       OR (status = 'HELD' AND hold_expires_at < NOW())
  );
```

이 방식으로 상태 검사와 업데이트를 DB에서 원자적으로 처리하여 동일 좌석 선점이 한 건만 성공하도록 개선했습니다.

---

## 동시성 테스트 (k6)

### 테스트 목적

동일 좌석에 대해 다수의 사용자가 동시에 선점 요청을 보낼 때,  
좌석 선점이 단 한 건만 성공하고 나머지는 충돌(409)로 처리되는지 검증했습니다.

### 테스트 환경

- 테스트 도구: k6
- 대상 API: `POST /sections/{sectionId}/seats/{seatId}/hold`
- 테스트 조건: 동일 좌석에 대해 100명 동시 요청

### 개선 전

- 성공(200): 10
- 충돌(409): 90

조회 후 수정 방식에서는 동시 요청 상황에서 다중 성공이 발생할 수 있음을 확인했습니다.

### 개선 후

- 성공(200): 1
- 충돌(409): 99
- total requests: 100
- p95 latency: 2.35s

DB 조건부 UPDATE 방식으로 선점 로직을 원자화한 뒤, 동일 좌석에 대해 한 건만 성공하고 나머지는 409로 차단되는 것을 확인했습니다.

---

## 선점 만료 처리

선점 시 `holdExpiresAt`에 만료 시간을 저장하고,  
만료된 HELD 상태는 다음 두 방식으로 해제되도록 설계했습니다.

- 좌석 조회 시 `releaseIfExpired()` 호출
- 스케줄러를 통해 주기적으로 만료 좌석 자동 해제

스케줄러 주기는 1분으로 두어 최대 1분 정도의 지연을 허용하는 대신 DB 부하를 과도하게 늘리지 않도록 했습니다.

---

## 설계 포인트

- 좌석 상태의 최종 권위는 Seat가 갖도록 설계
- Reservation은 예약 시도/이력 기록용으로 분리
- 상태 변경 로직은 Seat 엔티티 내부 메서드로 관리
- 동시성 문제는 애플리케이션 로직뿐 아니라 DB 조건부 UPDATE를 통해 최종 보장

---

## 향후 개선 계획

- confirm 동시성 테스트 추가
- sectionId와 seatId의 소속 관계 검증 강화
- 인덱스 튜닝 및 EXPLAIN 분석
- 예외 처리 공통화 (`@RestControllerAdvice`)
- 결제 도메인 분리 및 모킹 고도화