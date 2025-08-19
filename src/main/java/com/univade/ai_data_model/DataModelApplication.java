package com.univade.ai_data_model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataModelApplication.class, args);
	}
}