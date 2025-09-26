package io.github.bigpig.back.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@AllArgsConstructor
public class OpenApiConfiguration {

    private Environment environment;

    @Bean
    public OpenAPI defineOpenAPI () {
        Server server = new Server();
        String serverUrl = environment.getProperty("api.server.url");
        server.setUrl(serverUrl);
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("BiGPiG");
        myContact.setEmail("Bogdan.ars.15@yandex.ru");

        Info info = new Info()
                .title("BP Invest Backend API")
                .version("1.0")
                .description("Core backend API for BP Invest application")
                .contact(myContact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
