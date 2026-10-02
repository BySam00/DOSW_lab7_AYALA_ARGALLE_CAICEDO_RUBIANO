package edu.eci.dosw.oficioya.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("API de OficioYa")
                .version("1.0.0")
                .description("Documentación interactiva de los endpoints para la gestión de trabajadores, usuarios y servicios del sistema OficioYa."));
    }
}
