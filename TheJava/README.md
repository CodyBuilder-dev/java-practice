# 백기선의 The Java
## 자바 런타임 이해
### 자바의 컴파일 및 실행 흐름
자바 소스코드->(컴파일러)->자바 바이트코드->(JVM)-> OS 기계어 코드->(OS)->실행!

### Jar
- 자바 바이트코드 + 기타 필요 라이브러리

### JVM (Java Virtual Machine)
- 자바 바이트코드를 기계어코드로 변환
- 사실 class파일을 Java로 짜던, Groovy로 짜던,어떤 언어로 짰느냐는 묻지 않는다
    - 아무 class 파일이나 던져주면 다 기계어로 만들어서 실행시켜 준다
### JRE (Java Runtime Environment)
- 자바 어플리케이션 실행을 위한 최소한의 환경
- javac 미포함 -> 바이트코드 컴파일 불가
- java 11버전부터 JRE 미제공

### JDK (Java Development Kit)
- 자바 어플리케이션 실행 + 자바 개발을 위한 환경
- javac 포함
- Oracle JDK는 11버전부터 상용으로 사용될 경우 유료

## JVM 구조
### 아키텍처
- 클래스 로더
- 메모리
- 실행 엔진
- JNI
### JNI (Java Native Interface)
- 네이티브 메소드를 자바에서 실행시킬수 있도록 해줌
    - 네이티브 메소드는 native keyword가 붙은 메소드
    - C/C++로 구현됨
### 실행엔진
- 인터프리터
    - 바이트 코드를 한줄씩 읽으며 기계어로 컴파일
- JIT 컴파일러
    - 반복되는 코드를 바이트 코드를 기계어로 컴파일해줌
- GC
    - Stop The World/Throwput 방식
