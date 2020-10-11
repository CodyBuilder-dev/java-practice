# Servlet
---
## Servlet 매핑
### 개념
- Servlet 접근을 위한 Full path URL을 축약된 URL로 매핑시켜 주는 것
- 각 설정파일을 톰캣 컨테이너가 인식해 매핑시켜 주는 듯
### 방법1. web.xml을 이용한 설정
- 경로 : WebContent/WEB-INF/web.xml 내에 설정
- 설정 문법 : [설명 참조](https://velog.io/@max9106/JSP-Servlet-Mapping-jpk573o5yw)

### 방법2. Java Annotation을 이용한 설정
- 경로 : 각 서블릿 파일
- 설정 문법 : @WebServlet("/경로")

---
## Servlet Request/Response
### Servlet 객체
- 자바는 객체지향 프로그램이므로, Servlet도 객체이고, Req/Res또한 객체에 담겨져 처리됨
- Servlet 객체 상속 구조  
![](https://t1.daumcdn.net/cfile/tistory/99DE7A425C35CB162B)  
- Servlet 객체를 만들면, Eclipse가 HttpServletRequest/HttpServletResponse 객체를 넣어 자동완성 해준다

### HttpServletRequset/HttpServletResponse
- Apache Tomcat Doc : [Request](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletRequest.html) / [Response](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletResponse.html)

---
## Servlet LifeCycle
### 생명주기의 개념
![](https://media.vlpt.us/post-images/max9106/1c184800-33d7-11ea-8437-b39361a076dc/-2020-01-11-3.29.03.png)  
- 서블릿은, 항상 컨테이너 메모리에 올라와 있는 것이 아니라, 호출될때만 컨테이너상에 잠깐 올라왔다 호출처리가 완료되면 컨테이너상에서 내려가는 듯
- @PostConstruct : 
- init() : 
- service : 
- destory() : 
- @PreDestroy : 

### 생명주기 메소드의 구현
- 각 서블릿 클래스 내에 구현하면 된다
- @PostConstruct, @PreDestory Annotation 이용
- 메소드명을 init(), destroy()로 구현
- service는 doGet,doPost등에 기술해줌

### 방법2. Java Annotation을 이용한 설정

---
## Form 데이터 처리
### 개념
### 방법1. web.xml을 이용한 설정
### 방법2. Java Annotation을 이용한 설정

---
## Servlet 데이터 공유
### 개념
### 방법1. web.xml을 이용한 설정
### 방법2. Java Annotation을 이용한 설정