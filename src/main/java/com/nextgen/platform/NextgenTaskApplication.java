package com.nextgen.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages= "com.nextgen.platform")
@EnableAutoConfiguration()
public class NextgenTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextgenTaskApplication.class, args);
	}

}