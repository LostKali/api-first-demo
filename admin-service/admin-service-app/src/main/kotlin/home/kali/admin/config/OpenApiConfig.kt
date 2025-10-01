package home.kali.admin.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .openapi("3.0.3")
            .info(
                Info()
                    .title("Admin Service API")
                    .description("API для управления отчетами")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("API Support")
                            .email("support@example.com")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("Development server")
            )
            .addServersItem(
                Server()
                    .url("https://api.example.com")
                    .description("Production server")
            )
    }
}
