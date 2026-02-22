StudySpringBoot - Cart Service (Spring MVC)

Quickstart:

- Java 21
- Gradle
- Port: 8082

Run:

Windows:

    cd StudySpringBoot/cart
    ..\..\gradlew.bat bootRun

API:

- GET    /api/carts/user/{userId}                      - 사용자 장바구니 조회 (없으면 생성)
- GET    /api/carts/{id}                               - 장바구니 조회
- POST   /api/carts/user/{userId}/items                - 장바구니에 상품 추가
- PUT    /api/carts/user/{userId}/items/{productId}    - 장바구니 상품 수량 변경
- DELETE /api/carts/user/{userId}/items/{productId}    - 장바구니 상품 제거
- DELETE /api/carts/user/{userId}                      - 장바구니 비우기

Features:
- 사용자별 장바구니 자동 생성
- 동일 상품 추가시 수량 자동 증가
- 실시간 총액 계산

Examples:

    curl http://localhost:8082/api/carts/user/user1
    curl -X POST http://localhost:8082/api/carts/user/user1/items -H "Content-Type: application/json" -d "{\"productId\":\"1\",\"productName\":\"MacBook\",\"quantity\":1,\"price\":2500000}"
    curl -X PUT http://localhost:8082/api/carts/user/user1/items/1 -H "Content-Type: application/json" -d "{\"quantity\":3}"
    curl -X DELETE http://localhost:8082/api/carts/user/user1/items/1
    curl -X DELETE http://localhost:8082/api/carts/user/user1
