package readinglist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBootApplication 어노테이션을 적어줌으로서
// 스프링부트의 기능들 중 컴포넌트 검색과 자동 구성을 활성화한다
// @Configuration + @ComponentScan + @EnableAutoConfiguration
@SpringBootApplication 
public class ReadingListApplication {

	public static void main(String[] args) {
		// .run메소드를 통해 어플리케이션을 실행한다
		SpringApplication.run(ReadingListApplication.class, args);
	}

}
