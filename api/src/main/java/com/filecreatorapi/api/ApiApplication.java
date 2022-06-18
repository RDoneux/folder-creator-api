package com.filecreatorapi.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Utils.loadSettings();
		// SpringApplication.run(ApiApplication.class);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ApiApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);

		printMessage();
	}

	public static void printMessage() {
		System.out.println("");
		System.out.println("");
		System.out.println("--------------- LOADING APPLICATION - PLEASE WAIT ---------------");
		System.out.println("-------- This may take a little while please be patient ---------");
		System.out.println("");
		System.out.println("--- 'It's the third bounce that fixes it' - Paul White (2020) ---");
		System.out.println("");
		System.out.println("");
	}

}
