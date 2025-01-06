package portfolio.StudentManagement;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            "http://localhost:3000",
            "https://www.naomichiizumi.com",
            "https://naomichiizumi.com"
        )
        .allowedOriginPatterns("https://*-omi354s-projects.vercel.app")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}