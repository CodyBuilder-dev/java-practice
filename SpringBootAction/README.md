# 스프링 부트 코딩 공작소(Spring Boot in Action)
**스프링 개발이 다시 즐거워진다!!**
---
## 스프링 부트를 이루는 4가지 뼈대
1. 자동 구성 : 스프링 애플리케이션에 공통으로 필요한 기능 자동 구성
2. 스타터 의존성 : 기능을 제시하면 필요한 라이브러리를 자동으로 빌드에 추가한다.
3. 명령줄 인터페이스 :  빌드조차 필요없이 애플리케이션 실행이 가능하다
4. 엑추에이터 : 스프링 애플리케이션 실행 중 내부를 확인 가능하다

### 자동 구성(Automatic Configuration)
Bean간의 의존관계 연결을 자동으로 해준다!!  
우린 의존관계를 마음대로 설정하고 가져다 쓰기만 하면 된다  
(예시 : DAO)

### 스타터 의존성(Starter Dependency)
필요한 기능에 맞게 라이브러리 의존성이 미리 정의된 스타터 의존성을 제공한다.   
(예시 : Spring Web 스타터, JPA 스타터, Security 스타터)

### 명령줄 인터페이스
자바 클래스 파일이름만 명령줄에서 실행시키면, CLI가 알아서 실행에 필요한 모든 절차(빌드, 서버실행)을 알아서 실행시켜준다

### 엑추에이터
웹 UI/쉘 인터페이스를 통한 애플리케이션 상태 확인이 가능하다

### + Spring Initializer
기존의 스프링 프로젝트 구조를 자동으로 생성해주는 기능
(스프링부트를 사용하되, 기존 스프링 개발 구조를 그대로 사용하고 싶을 때 사용)
1. http://start.spring.io에 접속해 사용
2. 이클립스 기반 STS로 사용(결국 웹 initillizer에 API로 연결)
3. IntelliJ IDEA 사용(결국 웹 initillizer에 API로 연결)
4. 스프링 부트 CLI에서 사용(spring init 명령어 사용)

## 스프링 부트 예시 프로젝트(ReadingList) 프로젝트 구성 설명
### 프로젝트 폴더 구조
build.gradle : 그레이들 빌드 명세(maven의 pom.xml과 동일. 컴파일에 사용될 자바 버전도 여기서 설정)  
src/resources/application.properties : 어플리케이션의 프로퍼티 파일  
src/main/java/readinglist/ReadingListApplication : 스프링 부트스트래핑 파일(프로젝트 전체의 엔트리파일임)  
src/test/java/readinglist/ReadingListApplicationTests : 스프링부트 어플리케이션 테스트 파일

### 프로젝트 빌드하기
#### 스프링부트 스타터 라이브러리 버전 확인
gradle dependencies
#### 스타터 라이브러리 오버라이드
전이적 의존성 제외 : <exclusion>
명시적 버전 작성 : <dependency>에 버전 작성


### 프로젝트 작성하기

### 프로젝트 실행하기