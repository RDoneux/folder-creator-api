package com.filecreatorapi.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Utils.loadSettings();
		SpringApplication.run(ApiApplication.class);
	}

}
