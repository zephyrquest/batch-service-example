package com.github.zephyrquest.batch_service_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchServiceExampleApplication {

	public static void main(String[] args) {
		//SpringApplication.run(BatchServiceExampleApplication.class, args);
		System.exit(SpringApplication.exit(SpringApplication.run(BatchServiceExampleApplication.class, args)));
	}
}