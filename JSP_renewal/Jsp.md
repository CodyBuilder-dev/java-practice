# JSP
## JSP Script
### Eclipse JSP 템플릿을 HTML5 버전으로 만들기
- Window > Preference > Web > JSP File > Editor > Template > New > HTML5 양식 복붙
### JSP 선언 태그
`<%! 자바 문법 %>`
- 멤버변수 또는 메서드를 선언하기 위한 문법


### JSP 스크립트릿 태그
`<% 자바 문법 %>`
- 자바 코드를 넣기 위한 문법
- 코드가 굉장히 번잡해지므로, 복잡한 로직을 스크립트릿으로 구현하는 일은 적음


### JSP 주석 태그
`<%-- 주석 내용 --%>`
- JSP 컴파일 시점에 제외되어 컴파일
- HTML 주석과의 차이점
    - HTML 주석은 클라이언트 브라우저에서 확인 가능
    - JSP주석은 컴파일시점에 제외되므로 브라우저에서 확인 불가

### JSP 표현식 태그
`<%= 변수/반환값 %>`
- 변수값이나 메서드 반환값을 출력하기 위한 태그


### JSP 지시어
`<%@ page %>`
`<%@ include %>`
`<%@ taglib %>`
- 서버에서 JSP를 처리하는 방법에 대한 정의
- import구문도 여기 포함

---

## JSP Request/Response
### 개념
### 방법1. web.xml을 이용한 설정
### 방법2. Java Annotation을 이용한 설정

---
## JSP 내장 객체
### 개념
### 방법1. web.xml을 이용한 설정
### 방법2. Java Annotation을 이용한 설정

