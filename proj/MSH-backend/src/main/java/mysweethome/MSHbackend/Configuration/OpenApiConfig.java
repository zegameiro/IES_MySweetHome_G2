package mysweethome.MSHbackend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
@EnableWebMvc
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("My Sweet Home")
                    .description("MSH - API Endpoint Documentation")
                    .version("1.3.6"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("User API")
            .pathsToMatch("/user/**")
            .build();
    }

    @Bean
    public GroupedOpenApi sourcesApi() {
        return GroupedOpenApi.builder()
            .group("Data Sources API")
            .pathsToMatch("/sources/**")
            .build();
    }
}