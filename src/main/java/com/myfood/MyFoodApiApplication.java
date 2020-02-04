package com.myfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.myfood.infraestructure.CustomJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class MyFoodApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFoodApiApplication.class, args);
	}

}
