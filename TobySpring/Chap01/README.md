## Spring 메소드를 통한 DI 구현
### 생성자 메소드
### 수정자 메소드
### 초기화 메소드

### XML 파일을 통한 의존관계 설정
1. 메소드 설정
개념 : 메소드에 적용되는 @Bean 설정을 XML에 저장 가능  
양식 : 

    <bean id="메소드명" class="반환하는 오브젝트 full package 경로" />

2. 오브젝트 설정
개념 : 오브젝트를 XML에 설정해 저장 가능  
양식 : 

    <bean id="오브젝트명" class="오브젝트 full package 경로">
        <property name=">
    </bean>

3. 최종적으로 <beans>로 감싸주기
양식 : 

    <beans>
        <bean>...</bean>
    </beans>

## DataSource 인터페이스

### XML 파일을 통한 의존관계 설정
1. 접속정보를 property로 전달
개념 : DB연결을 위한 접속 정보(port, username,pw 등)을 클래스 외부에서 XML로 전달하기 위함  
양식 :

    <property name="수정자 메소드명" value="값" />

동작이 되는 이유 : 스프링이 XML Property의 value를 해당 수정자의 argument data type으로 적절히 전환해 주기 때문