# 프로젝트 개요

이 프로젝트는 Spring Boot 기반의 백엔드 서버로, 사용자 관리, 게시글, 결제 등 다양한 기능을 제공합니다.

---

## 시스템 구성도

```
[클라이언트(웹/앱)]
        │
        ▼
[Spring Boot API 서버]
        │
        ├─ Controller: 요청 처리
        ├─ Service: 비즈니스 로직
        ├─ Repository: DB 접근
        ├─ Configuration: 환경설정 및 보안
        ├─ Exception: 예외 처리
        ├─ Security: 인증/인가
        │
        ▼
[Database (예: MySQL)]
```

---

## 폴더 구조 및 상세 기능 설명

- **controller/**
  - API 엔드포인트를 정의하는 계층입니다.
  - 각 도메인(사용자, 게시글, 결제 등)별로 하위 폴더가 존재하며, HTTP 요청을 받아 서비스 계층에 전달합니다.
  - 예시:
    - `user/` : 회원가입, 로그인, 회원정보 조회/수정 등 사용자 관련 API
    - `post/` : 게시글 작성, 조회, 수정, 삭제 등 게시글 관련 API
    - `payment/` : 결제 요청, 결제 내역 조회 등 결제 관련 API
    - `AdminController.java` : 관리자 기능 관련 API

- **service/**
  - 비즈니스 로직을 처리하는 계층입니다.
  - Controller에서 받은 요청을 실제로 처리하며, 트랜잭션 관리, 데이터 가공, 외부 API 연동 등의 역할을 수행합니다.
  - 각 도메인별로 서비스 클래스가 존재합니다.
  - 예시:
    - `UserService.java` : 사용자 관련 비즈니스 로직
    - `PostService.java` : 게시글 관련 비즈니스 로직
    - `PaymentService.java` : 결제 관련 비즈니스 로직

- **repository/**
  - 데이터베이스 접근을 담당하는 계층입니다.
  - JPA, QueryDSL 등을 활용하여 엔티티의 CRUD 및 복잡한 쿼리 처리를 담당합니다.
  - 예시:
    - `UserRepository.java` : 사용자 엔티티 관련 DB 접근
    - `PostRepository.java` : 게시글 엔티티 관련 DB 접근
    - `CommentRepository.java`, `LikeRepository.java` 등

- **configuration/**
  - 프로젝트의 환경설정 관련 클래스를 모아둔 폴더입니다.
  - Spring Security, CORS, Swagger, RestTemplate 등 다양한 설정을 담당합니다.
  - 예시:
    - `SecurityConfig.java` : 인증/인가 및 보안 설정
    - `RestAuthenticationEntryPoint.java` : 인증 실패 시 처리 로직
    - `WebConfig.java` : CORS, 메시지 컨버터 등 웹 환경설정

- **exception/**
  - 커스텀 예외 클래스와 글로벌 예외 핸들러를 포함합니다.
  - 서비스 및 컨트롤러에서 발생하는 예외를 일관되게 처리하여, 클라이언트에 표준화된 에러 응답을 제공합니다.
  - 예시:
    - `GlobalExceptionHandler.java` : 전체 예외 처리
    - `AlreadyExistsException.java`, `ResourceNotFoundException.java`, `UnauthorizedException.java`, `UserStatusException.java` 등

- **security/**
  - JWT 기반 인증/인가, 토큰 발급 및 검증, 사용자 인증 관련 코드를 포함합니다.
  - 인증 필터, 토큰 유틸리티, 사용자 인증 정보 관리 등 보안 관련 핵심 로직이 위치합니다.
  - 예시:
    - `JwtTokenProvider.java` : JWT 토큰 생성 및 검증
    - `CustomUserDetailsService.java` : 사용자 인증 정보 로딩
    - `JwtAuthenticationFilter.java` : JWT 인증 필터

---

## 환경 설정 내역

- 환경별 설정 파일 분리 가능 (`application-dev.properties`, `application-prod.properties` 등)
- 환경에 따라 API 호출 및 설정 분기 처리 예시:
  - `application.properties`에서 `spring.profiles.active=dev` 또는 `prod`로 지정
  
