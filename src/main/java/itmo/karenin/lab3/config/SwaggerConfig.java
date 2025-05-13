package itmo.karenin.lab3.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server")
                ))
                .info(new Info()
                        .title("Pet Owner API")
                        .version("1.0.4")
                        .description("""
                                REST API для управления питомцами и их владельцами
                                ### Основные возможности:
                                - Получение информации о питомцах и владельцах
                                - Фильтрация и пагинация результатов
                                - Управление дружескими связями между питомцами
                                """));
    }
}