# 스프링 모듈
spring-core    
spring-aop  
spring-jdbc   
spring-tx  
spring-webmvc

## 스프링 모듈 설정의 자동화
필요한 모듈들의 목록을 
- maven : pom.xml
- gradle :   
에 넣어주기만 하면 된다

---
# 스프링 컨테이너
컨테이너 : 스프링에서 사용되는 객체들을 생성하고 조립해주는 객체  
빈(Bean) : 컨테이너가 만든 객체

## 빈 설정 파일 자동화
빈 설정 파일 : 컨테이너가 빈을 만들 수 있도록 빈의 내용들을 설정해둔 파일  
- maven :
- gradle : 

---
# maven 프로젝트 생성
Group ID : 프로젝트 전체를 포괄하는 고유 식별자  
Artifact ID : 프로젝트 빌드 결과로 나오는 jar파일명(모듈)  

## pom.xml 의 역할

## pom.xml에서 받아진 라이브러리 경로
users/.m2/repository/

## pom.xml 갱신하기
pom.xml 우클릭 > mavern > reload
---
# 스프링과 의존성 주입(DI)
## 의존성 주입의 개념
특정 객체 A가 특정 객체 B가 있어야만 온전히 동작할때, A가 B에 의존한다고 한다.  
이때 객체 A 내에서 B를 생성해서 사용할 경우, 비효율적이다.  
객체 A 외부에서 B를 주입하여 객체 A를 생성할 경우 이를 의존성 주입이라고 한다.
## 일반적인 Java Application의 의존성 주입
Controller-Service-DAO 등의 의존관계를 띄는 구조를 직접 설계  
각 객체마다 필요한 의존성을 수동으로 의존성 주입    
이때 필요한 모든 객체들을 import해서 사용해야 함

## 스프링의 의존성 주입 자동화
applicationContext.xml 파일에 모든 객체의 선언을 적어준다.  
해당 파일에 선언된 스프링의 모든 빈은 Spring Container(명칭 : Application Context)에 존재한다.  
객체가 필요할 때 마다 Spring Container의 getBean() 메소드로 불러와서 사용한다.

---
# 10강.스프링에서의 생명주기(LifeCycle)
## 스프링 컨테이너의 생명주기
- 생성 : 스프링 컨테이너 객체를 new를 이용해 생성한 시점에 메모리에 생성
- 소멸 : 스프링 컨테이너 객체를 .close로 종결한 경우

## 스프링 빈의 생명 주기
- 생성 : 스프링 컨테이너 생성 시점과 동일
- 소멸 : 스프링 컨테이너 소멸 시점과 동일

## 생명주기 처리 메소드 1
- 인터페이스 : InitializingBean/DisposableBean 인터페이스 내에 존재
- 메소드 : afterPropertiesSet/destroy
- 사용법 : 각 빈 객체내에서 해당 인터페이스를 implement하여 사용
- 응용예시 : DB연결시 인증작업 등

## 생명주기 처리 메소드 2
- XML속성 : init-method/destroy-method
- 사용법 : applicationContext.xml내의 빈 설정시, 각 XML속성을 정의해 줌
---
# 11~12강. 어노테이션(Annotation)
---
# 13~19강. 웹 MVC
---
# 20~21강. 인증과 권한
---
# 22~25강.데이터베이스
## 작업환경 설치
Oracle : 11g/18c EXPRESS EDITION
SQLDeveloper : 20.0.2(최신버전)
DB USER : scott/tiger

## DI없는 JDBC 사용방법
1. import java.sql.Connection
2. driverType/DB URL/ID/PW 정보를 통해 conn 생성
3. conn 객체 생성
4. sql문 작성 맟 preparestatement 로 치환변수 등록
5. 쿼리 실행
-> 위에 적힌 굉장히 복잡한 절차를 DAO에 매번 반복적으로 작성해야 한다

## 스프링 JDBC Template 통한 JDBC 연결 자동화
- DB Conn 생성/DB Conn 해제 부분을 템플릿화
    - 개발자는 오직 SQL 작성에만 신경쓰면 됨
- 사용법
    1. maven pom.xml에 org.springframework.spring-jdbc dependency 추가
    2. import DriverManagerDataSource
    3. DriverManagerDatasource 객체에 DB접근정보를 저장
    4. 해당 Datasource 객체를 JdbcTemplate객체에 연결
    5. JdbcTemplate 객체의 sql 호출 메소드에 sql문과 치환변수를 arg로 집어넣으면 끝

## 스프링 커넥션 풀
- 커넥션 풀이란?
    - 커넥션을 미리 만들어 놓고, 요청이 있을 때 선택해서 사용
    - 서버 부하 감소 효과
- 커넥션 풀 포함된 DataSource 이용
    - c3p0.ComboPooledDataSource
    - 반드시 예외처리 구문 내에서 connection 생성해야 함
- 스프링 설정파일 내의 DataSource Bean 이용
    - 설정파일 내에 bean을 생성할 때, 사용할 커넥션 풀 설정
