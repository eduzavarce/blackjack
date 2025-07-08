package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Blackjack API")
                .description("RESTful API for Blackjack game")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("Eduardo Zavarce")
                        .email("eduzavarce@gmail.com")
                        .url("https://eduzavarce.dev"))
                .license(
                    new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
  }
}
