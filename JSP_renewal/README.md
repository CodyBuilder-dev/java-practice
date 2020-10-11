# Java Web Programming With JSP/Servlet
## 프로젝트 시작하기
### JSP 프로젝트 생성
(Eclipse 기준으로 설명)  
- 프로젝트 생성 : New > Dynamic Web Project
- 프로젝트 파일 작성 : New > JSP 파일
    - WebContent폴더가 웹 컨테이너 폴더
    - 해당 폴더 아래에 JSP파일 작성
- 프로젝트 실행 : JSP파일 우클릭 > Run As > Run on the server
    - JSP파일은 변경해도 Tomcat 재기동 필요 없음
- 프로젝트 접근 : localhost:8080/프로젝트명/경로/파일명
    - 사용자는 HTML파일로 볼 수 있음

### JSP 파일 처리과정
- 개발자가 JSP파일 작성
- 톰캣 웹 컨테이너에서 JSP를 빌드
    - 빌드된 파일들은 **톰캣 설치 디렉토리 내**에 .class로 저장됨
- 사용자 Req에 따라 빌드된 obj파일을 실행시켜 HTML을 생성 후 Res 반환

### Servlet 프로젝트 생성
(Eclipse 기준으로 설명)  
- 프로젝트 생성 : JSP와 동일
- 프로젝트 파일 작성 : New > Servlet
    - 자동으로 src/패키지명 폴더 아래에 생성
    - 해당 java파일의 doGet/doPost메소드 내에 동작 작성
- 프로젝트 실행 : java파일 우클릭 > Run As > Run on the server
- 프로젝트 접근 : localhost:8080/프로젝트명/경로/파일명
    - 사용자는 HTML파일로 볼 수 있음

### Servlet 파일 처리과정
- 개발자가 java 파일 작성
- 톰캣 웹 컨테이너에서 java 빌드
    - 빌드된 파일들은 **프로젝트 build 디렉토리 내**에 .class로 저장됨
- 사용자 Req에 따라 빌드된 obj파일을 실행시켜 HTML을 생성 후 Res 반환