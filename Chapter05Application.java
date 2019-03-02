package cn.llz.chapter05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.llz")
public class Chapter05Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter05Application.class, args);
	}

}
