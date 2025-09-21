package com.redroosters.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

// Para servir canciones desede una carpeta local del servidor, Se puedan abrir directamente desde
// el navegador a traves de la ruta /media/audio....

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.uploads.audio-dir}")
    private String audioDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path path = Paths.get(audioDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/media/audio/**")
                .addResourceLocations("file:" + path.toString() + "/");
    }
}
