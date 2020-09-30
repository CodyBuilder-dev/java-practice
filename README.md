# java-practice
생존을 위한 Java 공부

## 목적
모니터링 프로젝트 진행 및 VOC 전자정부 프레임워크 개발을 위함

## 학습 기간
~~8/28~~ (추후에도 계속 공부해야 함)  
~2021년 연중
---
## 자바 개발 레퍼런스 정리
### 교재 추천
|교재명|설명|
|---|---|
|[스프링 부트 코딩 공작소](https://ridibooks.com/books/754019165)|입문용으로 그나마 가장 괜찮은 책이라는 평이 있다|
|[토비의 스프링](https://ridibooks.com/books/111017526)|너무나 유명한 책, 스프링 코어를 이해하려면 꼭 읽어야 하는 책, 그러나 입문용으론 별로인 책|
|[배워서 바로 쓰는 스프링 프레임워크](https://ridibooks.com/books/443000789)|입문자용으론 조금 어렵고, 챕터간 구성이 종속적이라는 평이 있다|
|[배워서 바로 쓰는 스프링 부트 2](https://ridibooks.com/books/443000785)|입문자용으로 어려울 수 있지만 실무자에게 레퍼런스로 괜찮다는 평|
|[스프링 5 레시피](https://ridibooks.com/books/443000629)|역시 스프링 코어 설명이 있고, 실무에서 만나는 문제들에 대한 레퍼런스로서 좋은 책, 그러나 입문용으론 별로인 책|
|[스프링 부트로 배우는 자바 웹 개발](https://ridibooks.com/books/852000676)|입문서는 아니고, 사진 상태 등 편집이 불친절하다는 평 있다|
|[쉽게 따라하는 자바 웹 개발](https://github.com/keesun/study/tree/master/book)|pdf가 오픈되어 있음. 기본적 지식에 대한 설명 없어 입문자용으론 부적절|
|[자바 웹을 다루는 기술](https://ridibooks.com/books/754025528?_s=search&_q=%EC%9E%90%EB%B0%94+%EC%9B%B9%EC%9D%84+%EB%8B%A4%EB%A3%A8%EB%8A%94+%EA%B8%B0%EC%88%A0)|설명위주로, 실질적인 코딩 구현이 안 된다는 말이 있음|
|[Spring Boot 프로젝트](https://goddaehee.tistory.com/category/3.%20%EC%9B%B9%EA%B0%9C%EB%B0%9C/3_1_3%20%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8)| 스프링부트 실습 페이지, 코드가 있어 코드를 따라가면서 배울 수 있다. |
|[Spring Boot 개념과 활용 - 백기선](https://velog.io/@max9106/series/Spring-Boot-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-%EA%B8%B0%EC%B4%88)|백기선 개발자님의 해당 인강 요약정리|
|[전자정부 프레임워크로 게시판 만들기](https://canelia09.tistory.com/category/%EC%A0%84%EC%9E%90%EC%A0%95%EB%B6%80%20%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EC%97%B0%EC%8A%B5)<br>[전자정부 프레임워크 교육자료](https://www.egovframe.go.kr/cop/bbs/selectBoardArticle.do?nttId=1569&bbsId=BBSMSTR_000000000004&menu=4)||

### 개발환경(Eclipse/전자정부)
|분류|항목|링크|
|---|---|---|
|eclipse|Spring 개발환경 설정|[링크](https://addio3305.tistory.com/32?category=772645)|
|eclipse|eclipse Subversion 설치|[링크](https://recipes4dev.tistory.com/155)|
|eclipse|eclipse에서 소스 비교부터 svn 커밋까지||
|eclipse|자동 빌드 설정|[링크](https://dololak.tistory.com/63)|
|전자정부 프레임워크|전자정부 프레임워크 이해|https://coding-restaurant.tistory.com/146|
|전자정부 프레임워크|내장 Spring Starter|https://offbyone.tistory.com/391|
|전자정부 프레임워크|Code Generation|[링크](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:dev2:imp:codegen)|
|전자정부 프레임워크|Tomcat lifecycle exception 해결|[링크](https://startsecure.tistory.com/55)|
|Eclipse|트러블슈팅 기록|[링크](ISSUE.md)|

### Eclipse 단축키
|단축키|설명|
|---|---|
|`Ctrl+Shift+R`|원하는 제목의 파일 열기|
|`Ctrl+H`| 파일 내용 찾기|
|`Ctrl+Shift+f`|자동 줄맞춤|
|`Ctrl+Shift+/`|범위 주석 처리|
|`Ctrl+Shift+\`|범위 주석 해제|

### 개발환경(VS Code)
|분류|항목|링크|
|---|---|---|
|VSCode|VSCode로 소스 비교||
|VSCode|VSCode에 SVN extension 설치하기||
|VSCode|Spring 개발환경 설정|[링크](https://sambalim.tistory.com/67)|
|VSCode|VSCode에서 Springboot 빌드하기|[링크](https://myjamong.tistory.com/123)|
|VSCode|VSCode에서 JUnit 사용하기||
|Winmerge|Winmerge로 소스 비교 및 내용 변경||

### Java 문법
|분류|항목|링크|
|---|---|---|
|Java|문자열 intern()|[링크](https://www.latera.kr/blog/2019-02-09-java-string-intern/)|
|Java|not 연산자(!)|[링크](https://www.baeldung.com/java-using-not-in-if-conditions)|
|Java|FileReader와 BufferReader의 차이점||
|Java|java.time.LocalDateTime|[링크](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html)|
|Java|동일성과 동등성|[링크](https://joont.tistory.com/143)|
|Java|변수의 객체 클래스/데이터타입 확인법|[링크](https://wakestand.tistory.com/186)|
|Java|List|[링크](https://wikidocs.net/207)<br>|
|Java|Hashmap|[링크](https://junjangsee.github.io/2019/09/18/java/hashmap-Method/)<br>[반복문](https://lts0606.tistory.com/149)|

### Java 웹 개발
|분류|항목|링크|
|---|---|---|
|Java|Maven의 이해|[링크](https://goddaehee.tistory.com/199)<br>[폴더 구조]()<br>[빌드 라이프사이클](https://jeong-pro.tistory.com/168)|
|Java|Maven 커맨드라인 옵션|[링크](https://m.blog.naver.com/PostView.nhn?blogId=kathar0s&logNo=10143790065&proxyReferer=https:%2F%2Fwww.google.com%2F)|
|Java|MavenWrapperMain 에러 해결|[링크](https://www.slipp.net/questions/585)|
|Java|Maven package시 test 건너뛰기|[링크](http://www.devkuma.com/books/pages/642)|
|Java|Maven 빌드시 java.security.InvalidAlgorithmParameterException 해결|[링크](https://sarc.io/index.php/forum/tips/3102-openjdk-javax-net-ssl-sslexception-java-lang-runtimeexception-unexpected-error-java-security-invalidalgorithmparameterexception)|
|Java|Maven Profile 설정으로 환경에 맞게 build하기|[링크](https://www.lesstif.com/java/maven-profile-14090588.html)|
|Java|jar 명령어|[링크](http://blog.daum.net/oraclejava/15869132)|
|Java|jUnit 이용 단위 테스트||
|Java|Mock 객체를 이용한 테스트||
|Java|Batch 테스트||
|Java|단위 테스트에서의 F.I.R.S.T원칙|[링크](https://brunch.co.kr/@springboot/207)|
|Java|통합 테스트와 서비스 테스트|[링크](https://cheese10yun.github.io/spring-guide-test-1/)|
|Java|JSP란 무엇인가?|[링크](https://javacpro.tistory.com/43)|
|Java|JSP 문법|[링크](https://atoz-develop.tistory.com/entry/JSP-%EA%B8%B0%EB%B3%B8-%EB%AC%B8%EB%B2%95-%EC%B4%9D-%EC%A0%95%EB%A6%AC-%ED%85%9C%ED%94%8C%EB%A6%BF-%EB%8D%B0%EC%9D%B4%ED%84%B0-JSP-%EC%A0%84%EC%9A%A9-%ED%83%9C%EA%B7%B8-%EB%82%B4%EC%9E%A5-%EA%B0%9D%EC%B2%B4)|
|Java|JSP 예외처리|[링크](https://gangzzang.tistory.com/entry/JSP-%EC%97%90%EB%9F%AC-%EC%B2%98%EB%A6%AC%EC%9D%B5%EC%85%89%EC%85%98-%EC%B2%98%EB%A6%AC)<br>[링크](https://rongscodinghistory.tistory.com/70)|
|Java|Servlet 이해|[링크](https://snoopy81.tistory.com/313)|
|Java|JSP와 Servlet의 역할 구분(MVC2)|[링크](https://m.blog.naver.com/acornedu/221128616501)|
|Java|간단한 Servlet 페이지 예시|[링크](https://snoopy81.tistory.com/313)|
|Java|Apache Web Server의 이해||
|Java|Tomcat WAS 이해||
|Java|Connection Pool 의 이해|https://www.holaxprogramming.com/2013/01/10/devops-how-to-manage-dbcp/|
|Java|HikariCP의 이해|https://brunch.co.kr/@jehovah/24|
|Java|JPA란 무엇일까?|[링크](https://bit.ly/3ib0zDP) |
|Java|iBatis란 무엇일까?|[링크](https://jonghoit.tistory.com/73)|
|Java|iBatis와 MyBatis 차이|[링크](https://uwostudy.tistory.com/19)|
|Java|Hibernate란?|[링크](https://victorydntmd.tistory.com/195)|
|Java|하이버네이트 Dialect의 의미||
|Java|JPA & Spring JPA & Hibernate 구분|[링크](https://suhwan.dev/2019/02/24/jpa-vs-hibernate-vs-spring-data-jpa/)|
|Java|MyBatis의 이해||
|Java|MyBatis 사용법||
|Java|Generic이란?|[제네릭 개념](https://arabiannight.tistory.com/entry/%EC%9E%90%EB%B0%94Java-ArrayListT-%EC%A0%9C%EB%84%A4%EB%A6%AD%EC%8A%A4Generics%EB%9E%80)<br>[제네릭을 사용해야 하는 이유](https://cornswrold.tistory.com/180)|

### Spring
|분류|항목|링크|
|---|---|---|
|개요|Spring의 이해, Spring Boot의 차이|[링크](https://sas-study.tistory.com/274)<br>[링크](https://monkey3199.github.io/develop/spring/2019/04/14/Spring-And-SpringBoot.html)<br>[차이 전혀 없음](https://okky.kr/article/312710)<br>[차이점](https://stackoverflow.com/questions/32922914/difference-between-spring-mvc-and-spring-boot)<br>[간단해진 설정](https://ellune.tistory.com/38)|
|개요|MessageSource|[링크](https://engkimbs.tistory.com/717)|
|설정|스프링에서 web.xml의 의미|[링크](https://gmlwjd9405.github.io/2018/10/29/web-application-structure.html)|
|설정|beans,bean XML설정 옵션|[링크](https://atoz-develop.tistory.com/entry/Spring-%EC%8A%A4%ED%94%84%EB%A7%81-XML-%EC%84%A4%EC%A0%95-%ED%8C%8C%EC%9D%BC-%EC%9E%91%EC%84%B1-%EB%B0%A9%EB%B2%95-%EC%A0%95%EB%A6%AC)|
|빌드|Spring Gradle 구현 예제|[링크](https://github.com/stunstunstun/awesome-spring-boot)|
|어노테이션|@Autowired 이해|[링크](https://galid1.tistory.com/512)|
|어노테이션|@ModelAttribute 이해|[링크](https://engkimbs.tistory.com/694)|
|어노테이션|@Entity의 이해|[링크](https://www.icatpark.com/entry/JPA-%EA%B8%B0%EB%B3%B8-Annotation-%EC%A0%95%EB%A6%AC)|
|어노테이션|@GeneratedValue의 이해||
|Spring MVC|Spring MVC 플로우 정리|[링크](https://www.slideshare.net/hanmomhanda/spring-mvc-fullflow)|
|Spring MVC|Spring MVC View 처리 이해|[링크](https://gocoder.tistory.com/1104)|
|Spring MVC|Entity vs VO vs DTO|[링크](https://medium.com/webeveloper/entity-vo-dto-666bc72614bb)|
|Spring MVC|Dispatcher Servlet의 의미|[링크](https://mangkyu.tistory.com/18)|
|Spring MVC|Model, ModelandView 객체|[링크](https://velog.io/@msriver/Spring-Model-%EA%B0%9D%EC%B2%B4)|
|Spring MVC|.do를 생략한 URL pattern|[링크](https://okky.kr/article/293531)|
|Spring Batch|스프링 배치 확인법|[링크]()|
|DB|hikari CP 설정|[링크](https://javacan.tistory.com/entry/spring-boot-2-hikaricp-property)|
|DB|JPA Repository의 이해||
|DB|Spring 방식 JDBC 연동|[링크](https://www.holaxprogramming.com/2015/10/16/spring-boot-with-jdbc/)|
|아키텍처|프론트 컨트롤러 패턴|[링크](https://opentutorials.org/module/3569/21219)|


### Spring Boot
|분류|항목|링크|
|---|---|---|
|개요|이제는 Spring Boot를 써야할 때|[링크](https://feeva.github.io/posts/%EC%9D%B4%EC%A0%9C%EB%8A%94-spring-boot%EB%A5%BC-%EC%8D%A8%EC%95%BC%ED%95%A0-%EB%95%8C%EB%8B%A4)|
|개요|Spring Boot로 시작하는 프레임워크|[링크](https://futurecreator.github.io/2016/06/18/spring-boot-get-started/)|
|WAS|Spring boot 내장 WAS 설정|[링크](https://engkimbs.tistory.com/755)|
|아키텍처|Spring Boot 쿠버네티스 컨테이너화|[링크](https://futurecreator.github.io/2019/01/19/spring-boot-containerization-and-ci-cd-to-kubernetes-cluster/)|
|아키텍처|스프링 부트를 이용한 MSA|[링크](https://waspro.tistory.com/451)|
|View|Spring boot에서 jsp 실행시키기|[링크](https://antdev.tistory.com/27)|
|DB|Spring boot에 MySQL 연동|[링크](https://victorydntmd.tistory.com/321)|
|DB|Spring boot에 H2DB+mybatis 연동|[링크](http://tech.javacafe.io/2018/07/31/mybatis-with-spring/)|

### WebSquare(UI 프레임워크)
|분류|항목|링크|
|---|---|---|
|WebSquare|이클립스 설치|[링크](http://docs1.inswave.com/developerguide#0315c4bfa2106d9e)|
|WebSquare|엔진 동작 구조|![]()<br>![](http://docs1.inswave.com/r/book_image/get/724fd56689fd8db0?0)<br>![](http://docs1.inswave.com/r/book_image/get/49a9786f08253ce6?0)<br>![](http://docs1.inswave.com/r/book_image/get/d6a800686326b54f?1)|
|WebSquare|WYSIWYG 에디터 설명|[링크](http://docs1.inswave.com/developerguide#999029cb9a4f098e)|
|WebSquare|개발 방법 설명|[링크](https://einhugo.github.io/2019/08/05/Web/190805%20-%20WebSquare/)|
|WebSquare|웹스퀘어 + 톰캣 설정|[링크](http://docs1.inswave.com/developerguide#1b2c6f5188a85e65)|
|WebSquare|XML 구조|![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FZb70S%2FbtqAMOjcxOT%2F3ET6kQ3bLpEVD9lLvk5MH0%2Fimg.png)<br>[링크](http://docs1.inswave.com/developerguide#fc8be9599575c2e8)|


### 기타
|분류|항목|링크|
|---|---|---|
|XML|XML 문서 양식(DTD와 스키마)|[링크](http://tcpschool.com/xml/xml_dtd_intro)|
|XML|XML 에서 CDATA태그|[링크](https://parkjuwan.tistory.com/156)|
|XML|XForms 이해||
|XML|XPath 이해||
