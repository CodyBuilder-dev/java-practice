# 스프링 프로젝트
## 스프링 MVC구조 이해
![](mvc.png)

## 스프링 폴더 구조 파악
### pom.xml
프로젝트 내 Dependency 목록을 저장하는 파일

### 프로젝트명/src/main/java 
프로젝트 내 자바 소스 코드

### 프로젝트명/src/main/resources
프로젝트 내 설정 파일(application.properties)

### 프로젝트명/src/main/webapp/WEB-INF/views
프로젝트내 jsp 파일의 기본경로

### 프로젝트명/src/main/webapp/WEB-INF/web.xml
정식 명칭 : 서블릿 배포 기술자(Deployment Descriptor)  
동작 원리 : WAS 최초 구동시, web.xml을 읽어 WAS 설정 구성

### 프로젝트명/src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml
서블릿 관련 설정

## 스프링 Annotation 파악
### @RequestMapping 
목적 : HTTP Request와 Controller를 Mapping하기 위함
문법 :  
    @RequestMapping(value='/', method=RequestMethod.GET)

### @Controller 
목적 : 컨트롤러 클래스를 구분하기 위함

## 스프링 Controller 파악
###  model.addAttribute("변수명",값);
jsp파일 내의 변수에 값을 전달한다.

### return jsp파일
최종적으로 jsp view를 반환한다.


# 2. 스프링 부트 프로젝트
## 스프링 부트 view 만들기
### 스프링부트 view/template engine 종류
* JSP/JSTL
* Thymeleaf
* FreeMarker
* Velocity
* Groovy Template Engine
* Tiles

### 스프링부트 JSP 제한사항
- war packaging을 사용하면 JSP 사용가능  
- jar packaging을 사용하면 JSP 사용불가
- src/main/resource/template 폴더에 JSP 넣어도 동작 안함
- JSP 사용을 위해 pom.xml에 추가적인 처리 필요

### 스프링부트 JSP 폴더 생성
src/main/resources/WEB-INF/jsp  
또는 src/main/resources/WEB-INF/views

### 스프링부트 application.properties 파일 변경


## 스프링부트 자동 파일변경 반영하기