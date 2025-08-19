// WebConfig.java
package com.tuscany.tour.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/gallery/**")
                .addResourceLocations("file:uploads/gallery/");
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                /* registry.addMapping("/api/auth/public/**") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);*/
                registry.addMapping("/api/csrf-token") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);
                registry.addMapping("/api/auth/public/**") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);
                registry.addMapping("/api/auth/**") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);
                registry.addMapping("/api/tours/**") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);
                /* registry.addMapping("/**") // Allow all paths
                        .allowedOrigins(frontendUrl)
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow all headers example : Authorization, Content-Type, etc.
                        .allowCredentials(true) // Allow credentials such as cookies, authorization headers, or TLS client certificates
                        .maxAge(3600);*/
            }
        };
    }
}


