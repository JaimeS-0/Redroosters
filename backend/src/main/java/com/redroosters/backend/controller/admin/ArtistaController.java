package com.redroosters.backend.controller.admin;

import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.service.AlmacenamientoService;
import com.redroosters.backend.service.ArtistaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



// Crear, Editar, Eliminar Artista
@RestController
@RequestMapping("/api/admin")
public class ArtistaController  {

    private final ArtistaService artistaService;
    private final AlmacenamientoService almacenamientoService;

    public ArtistaController(ArtistaService artistaService,
                             AlmacenamientoService almacenamientoService) {
        this.artistaService = artistaService;
        this.almacenamientoService = almacenamientoService;
    }

    @PostMapping(
            value = "/artistas",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ArtistaResponseDTO> crearArtista(
            @RequestPart("nombre") String nombre,
            @RequestPart(value = "descripcion", required = false) String descripcion,
            @RequestPart(value = "destacado", required = false) String destacadoStr,
            @RequestPart(value = "portada", required = false) MultipartFile portada
    ) {

        boolean destacado = false;
        if (destacadoStr != null) {
            destacado = Boolean.parseBoolean(destacadoStr);
        }

        String portadaUrl = null;
        if (portada != null && !portada.isEmpty()) {
            portadaUrl = almacenamientoService.guardarPortada(portada);
        }

        // Por si urlNombre es NOT NULL en la BBDD:
        String urlNombre = nombre
                .toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9\\-]", "");

        ArtistaRequestDTO dto = new ArtistaRequestDTO(
                nombre,
                urlNombre,
                descripcion,
                portadaUrl,
                destacado
        );

        ArtistaResponseDTO creado = artistaService.crearArtista(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Editar artista (con posible nueva portada)
    @PutMapping(
            value = "/artistas/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ArtistaResponseDTO> editarArtista(
            @PathVariable Long id,
            @RequestPart(value = "nombre", required = false) String nombre,
            @RequestPart(value = "descripcion", required = false) String descripcion,
            @RequestPart(value = "destacado", required = false) String destacadoStr,
            @RequestPart(value = "portada", required = false) MultipartFile portada
    ) {

        boolean destacado = false;
        if (destacadoStr != null) {
            destacado = Boolean.parseBoolean(destacadoStr);
        }

        String portadaUrl = null;
        if (portada != null && !portada.isEmpty()) {
            portadaUrl = almacenamientoService.guardarPortada(portada);
        }

        String urlNombre = null;
        if (nombre != null && !nombre.isBlank()) {
            urlNombre = nombre
                    .toLowerCase()
                    .replace(" ", "-")
                    .replaceAll("[^a-z0-9\\-]", "");
        }

        ArtistaRequestDTO dto = new ArtistaRequestDTO(
                nombre,
                urlNombre,
                descripcion,
                portadaUrl,   // null = no tocar portada si así lo decides en el servicio
                destacado
        );

        ArtistaResponseDTO actualizado = artistaService.editar(id, dto);
        return ResponseEntity.ok(actualizado);
    }



    // Eliminar artista
    @DeleteMapping("/artistas/{id}")
    //@Override
    public ResponseEntity<Void> eliminarArtista(@PathVariable Long id) {
        artistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
