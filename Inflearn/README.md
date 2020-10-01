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
# 