package com.gecm.lostfound.config;

import com.gecm.lostfound.service.FileStorageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final FileStorageService fileStorageService;

    public WebConfig(AuthInterceptor authInterceptor, FileStorageService fileStorageService) {
        this.authInterceptor = authInterceptor;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = fileStorageService.getUploadDir().toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath.endsWith("/") ? uploadPath : uploadPath + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/dashboard/**", "/admin/**");
    }
}
