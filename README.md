# 🖥️ Java Socket Chat Server (V1 → V2 개선 기록)

## 📌 프로젝트 개요
이 프로젝트는 **Java 소켓 프로그래밍**을 학습하며 만든 채팅 서버/클라이언트 애플리케이션입니다.  
버전 1에서는 간단히 동작하는 GUI 채팅을, 버전 2에서는 **명령어 기반 콘솔 채팅**으로 발전시켰습니다.  

---

## 🔄 개선 여정

### 1️⃣ V1: 최소 동작하는 GUI 채팅
- Swing 기반 GUI (텍스트 입력 + 버튼 + 메시지 창)  
- 서버는 단순히 클라이언트 메시지를 받아 전체 브로드캐스트  
- 클라이언트 종료 시 `"exit"` 문자열만 체크 → 확장성 부족  
- 소켓/스레드 관리가 중복되어 코드 가독성이 떨어짐  

**문제점**
- 프로토콜이 명확하지 않아 기능 확장 시 조건문이 계속 늘어남  
- 소켓 자원 정리 누락으로 서버 종료 시 문제 발생  

---

## V1 실행
**클라이언트 gui**

<img width="585" height="589" alt="image" src="https://github.com/user-attachments/assets/67cb91de-9cad-4933-8796-96fbcf3a90c3" />


**서버 터미널**

<img width="1124" height="508" alt="image" src="https://github.com/user-attachments/assets/9d3ed4c9-a94d-4cf5-8df7-8e4a42075d13" />

---

### 2️⃣ V2: 구조적 개선과 명령어 프로토콜 도입
- **콘솔 기반 명령어 체계화**
  - `/join|{username}` : 사용자 등록  
  - `/message|{text}` : 메시지 전송  
  - `/change|{username}` : 이름 변경  
  - `/users` : 현재 접속자 목록 확인  
  - `/exit` : 안전 종료  
- **세션 관리 강화**
  - `Session` 객체 + `SessionManager` 도입  
  - 접속자 관리, 사용자 목록 반환 기능 추가  
- **자원 관리 유틸화**
  - `SocketCloseUtil`로 소켓/스트림 정리 코드 중복 제거  
- **안정적 서버 종료**
  - `ShutdownHook`으로 서버 강제 종료 시 클린업 처리  
- **유지보수성 향상**
  - `CommandOption` Enum을 통해 명령어 확장 용이  
  - 기능 추가 시 간단히 분기만 추가하면 확장 가능  


## V2 실행

**클라이언트 서버 터미널 + 외부네트워크 사용자**
<img width="1811" height="1027" alt="image" src="https://github.com/user-attachments/assets/c211f473-313c-41f7-bc5f-5d3728c1867e" />


**개선 효과**
- 프로토콜이 체계화되어 클라이언트/서버 모두 직관적으로 동작  
- 새로운 명령어 추가가 간단해짐 (예: 귓속말, 파일 전송 등 확장 가능)  
- 서버 종료 시 안정적으로 세션과 소켓이 정리됨  

---

## 🚀 앞으로의 개선 방향
- [ ] **메시지 기록 저장**: 서버에서 로그 파일 또는 DB에 채팅 기록 저장  
- [ ] **권한 시스템**: 관리자(Admin) 계정에서 특정 명령어(`kick`, `ban`) 수행 가능  
- [ ] **비동기 입출력 (NIO)**: 현재는 blocking I/O 기반 → 대규모 사용자 확장 시 NIO 적용  
- [ ] **멀티룸 지원**: `join|room1` 처럼 특정 채팅방 생성 및 참가 가능  

---

## 📚 실행 방법

### 서버 실행
```bash
javac -d out src/server/*.java
java -cp out server.ServerMain





