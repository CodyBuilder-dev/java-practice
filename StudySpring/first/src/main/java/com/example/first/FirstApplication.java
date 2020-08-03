package com.example.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* @SpringBootApplication
= @EnableAutoConfiguration + @ComponentScan + @Configuration
(스프링부트 프로젝트 전체의 엔트리 클래스) 
*/
@SpringBootApplication
public class FirstApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstApplication.class, args);
	}

}
