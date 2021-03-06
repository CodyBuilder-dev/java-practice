# 1. 자바 개발환경 설치 트러블슈팅
## 이클립스 폴더 구조
configuration/.settings : 세팅 폴더

## Maven 폴더 구조
maven/repository

# 2. iBatis 한글 쿼리 깨짐 문제
## 문제점
SQL.xml내 쿼리문에 한글이 포함된 경우 받아오는 한글이 깨짐

## 원인분석
(1) Java -> DB
ibatis XML의 인코딩 확인

(2) DB -> Java
JDBC의 오라클 DB 내 한글데이터 Java 단 전송 로직[링크](https://m.blog.naver.com/PostView.nhn?blogId=stork838&logNo=220291872912&proxyReferer=https:%2F%2Fwww.google.com%2F)
## 해결시도
### 시도 1. XML 내에 한글을 쓸 수 있도록 시도
(1) XML파일의 인코딩을 EUC-KR로 변경  
결과 : 실패, 여전히 한글이 깨진 채로 SQL이 DB로 넘어가는 것 확인

(2) JDBC 설정 변경  
결과 : 기존 프로젝트의 설정을 건드리는 것은 매우 위험하므로 시도하지 않음

(3) iBatis 설정 변경  
결과 : 기존 프로젝트의 설정을 건드리는 것은 매우 위험하므로 시도하지 않음

-> 결론, 쿼리에 한글을 쓰는 모든 시도 실패
### 시도 2. DB 접근 전 자바단에서 쿼리문에 한글을 주입한다?
시도해보지 않음

### 시도 3. DB 접근 후 결과값을 파싱해서 수작업으로 고치기
(1) Java BE단에서 처리
- 방식 : DAO호출로 생성된 list와 Hashmap을 Java로 파싱 
- 결과 : 정상적으로 작동
- 장점 : 클라이언트 사이드에 부하전가가 적음. XML 파싱을 위한 추가 라이브러리 등 필요없이 built-in 메소드로 처리 가능
- 단점 : 디버깅 시 많은 시간 소요. 타 페이지들과의 컨벤션 불일치(타 페이지는 화면단에서 JS로 처리함)

(2) JavaScript FE에서 처리
- 방식 : Response로 받아온 XML을 Javascript로 처리
