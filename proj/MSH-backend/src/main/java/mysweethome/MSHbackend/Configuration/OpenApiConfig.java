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

    @Bean
    public GroupedOpenApi routinesApi() {
        return GroupedOpenApi.builder()
            .group("Routines and Actions API")
            .pathsToMatch("/routines/**")
            .build();
    }

    @Bean
    public GroupedOpenApi roomApi() {
        return GroupedOpenApi.builder()
            .group("Room API")
            .pathsToMatch("/room/**")
            .build();
    }

    @Bean
    public GroupedOpenApi outputDevApi() {
        return GroupedOpenApi.builder()
            .group("Output devices API")
            .pathsToMatch("/outputs/**")
            .build();
    }

    @Bean
    public GroupedOpenApi dataApi() {
        return GroupedOpenApi.builder()
            .group("Saved Data API")
            .pathsToMatch("/data/**")
            .build();
    }

    @Bean
    public GroupedOpenApi alertsApi() {
        return GroupedOpenApi.builder()
            .group("Alerts API")
            .pathsToMatch("/alerts/**")
            .build();
    }

    @Bean
    public GroupedOpenApi statsApi() {
        return GroupedOpenApi.builder()
            .group("Stats API")
            .pathsToMatch("/stats/**")
            .build();
    }
}