StudySpringBoot - Search Service (WebFlux)

Quickstart:

- Java 21
- Gradle
- Port: 8083

Run:

Windows:

    cd StudySpringBoot/search
    ..\..\gradlew.bat bootRun

API:

- GET    /api/search?keyword={keyword}           - 키워드로 상품 검색
- GET    /api/search/category/{category}         - 카테고리로 상품 검색
- GET    /api/search/price?minPrice=&maxPrice=   - 가격 범위로 검색
- POST   /api/search/index                        - 상품 인덱싱
- DELETE /api/search/{id}                         - 상품 인덱스 삭제

Examples:

    curl http://localhost:8083/api/search?keyword=apple
    curl http://localhost:8083/api/search/category/Electronics
    curl http://localhost:8083/api/search/price?minPrice=100000&maxPrice=2000000

