StudySpringBoot - Notification Service (WebFlux)

Quickstart:

- Java 21
- Gradle
- Port: 8084

Run:

Windows:

    cd StudySpringBoot/notification
    ..\..\gradlew.bat bootRun

API:

- POST /api/notifications              - 알림 전송
- POST /api/notifications/bulk         - 대량 알림 전송
- GET  /api/notifications/{id}         - 알림 조회
- GET  /api/notifications/user/{userId} - 사용자별 알림 조회
- GET  /api/notifications/pending      - 전송 대기 알림 조회
- GET  /api/notifications              - 모든 알림 조회

Examples:

    curl -X POST http://localhost:8084/api/notifications -H "Content-Type: application/json" -d "{\"userId\":\"user1\",\"type\":\"EMAIL\",\"recipient\":\"user@example.com\",\"subject\":\"Welcome\",\"content\":\"Hello!\"}"
    curl http://localhost:8084/api/notifications/user/user1

