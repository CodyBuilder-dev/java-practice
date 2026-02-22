StudySpringBoot - Order Service (Spring MVC)

Quickstart:

- Java 21
- Gradle
- Port: 8081

Run:

Windows:

    cd StudySpringBoot/order
    ..\..\gradlew.bat bootRun

API:

- POST   /api/orders                    - 주문 생성
- GET    /api/orders/{id}               - 주문 조회
- GET    /api/orders/user/{userId}      - 사용자별 주문 조회
- GET    /api/orders/status/{status}    - 상태별 주문 조회
- GET    /api/orders                    - 전체 주문 조회
- PUT    /api/orders/{id}/status        - 주문 상태 변경
- POST   /api/orders/{id}/cancel        - 주문 취소
- DELETE /api/orders/{id}               - 주문 삭제

Order Status:
- PENDING: 결제 대기
- CONFIRMED: 주문 확정
- SHIPPED: 배송 중
- DELIVERED: 배송 완료
- CANCELLED: 주문 취소

Examples:

    curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d "{\"userId\":\"user1\",\"shippingAddress\":\"서울시 강남구\",\"items\":[{\"productId\":\"1\",\"productName\":\"MacBook\",\"quantity\":1,\"price\":2500000}]}"
    curl http://localhost:8081/api/orders/user/user1
    curl -X PUT http://localhost:8081/api/orders/1/status -H "Content-Type: application/json" -d "{\"status\":\"CONFIRMED\"}"
