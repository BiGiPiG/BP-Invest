package io.github.bigpig.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

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

	@Bean
	public RestClient restClientBean() {
		return RestClient.create();
	}
}
