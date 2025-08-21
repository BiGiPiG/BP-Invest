package io.github.bigpig.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the REST API application.
 */
@SpringBootApplication
public class RestApiApplication {

	/**
	 * Application start method.
	 */
	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

}
