package com.redroosters.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AlmacenamientoService {

    private final Path root = Paths.get("/app/uploads/portadas");

    public String guardarPortada(MultipartFile archivo) {
        try {
            // Crear carpeta si no existe
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            // Nombre único
            String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

            // Ruta destino
            Path destino = root.resolve(nombreArchivo);

            // Guardar archivo
            archivo.transferTo(destino.toFile());

            // URL accesible desde NGINX
            return "/media/portadas/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar portada", e);
        }
    }
}
