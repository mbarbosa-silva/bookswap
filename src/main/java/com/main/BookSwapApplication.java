package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.entity","com.entity.resource"})
@EnableJpaRepositories("com.repository")
@ComponentScan({
	"com.main",
	"com.controller",
	"com.service"
})
public class BookSwapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSwapApplication.class, args);
	}

}
