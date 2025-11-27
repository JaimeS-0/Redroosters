package com.redroosters.backend.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redroosters.backend.docs.admin.CancionAdminApi;
import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.CancionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

// Crear, Editar, Eliminar Canciones

@RestController
@RequestMapping("/api/admin")
public class CancionController  {

    private final CancionService cancionService;
    private final ObjectMapper objectMapper;

    public CancionController(CancionService cancionService, ObjectMapper objectMapper) {
        this.cancionService = cancionService;
        this.objectMapper = objectMapper;
    }

    // Crear cancion con archivo (multipart) .mp3

    @PostMapping(value = "/cancion/archivo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    //@Override
    public ResponseEntity<CancionResponseDTO> crearConArchivo(
            @RequestPart("datos") String datosJson,
            @RequestPart("audio") MultipartFile audio,
            @RequestPart(value = "portada", required = false) MultipartFile portada

    ) {

        // Validacion que sea una cancion MP3
        String fileName = audio.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".mp3")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener extension .mp3");
        }

        String contentType = audio.getContentType();
        if (contentType != null && !contentType.startsWith("audio/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe ser de tipo audio (mp3).");
        }

        final CancionRequestDTO dto;
        try {
            dto = objectMapper.readValue(datosJson, CancionRequestDTO.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La parte 'datos' debe ser JSON válido (application/json)");
        }

        if (dto.artistaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "artistaId es obligatorio");
        }
        if (audio == null || audio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes adjuntar el archivo de audio en la parte 'audio'");
        }

        var resp = cancionService.crearConArchivo(dto, audio, portada);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // Editar CON posible cambio de audio/portada (multipart)
    @PutMapping(
            value = "/cancion/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CancionResponseDTO> editarCancion(
            @PathVariable Long id,
            @RequestPart("datos") String datosJson,
            @RequestPart(name = "audio", required = false) MultipartFile audio,
            @RequestPart(name = "portada", required = false) MultipartFile portada
    ) {
        final CancionRequestDTO dto;
        try {
            dto = objectMapper.readValue(datosJson, CancionRequestDTO.class);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La parte 'datos' debe ser JSON valido"
            );
        }

        var resp = cancionService.editarCancion(id, dto, audio, portada);
        return ResponseEntity.ok(resp);
    }




    // Eliminar
    @DeleteMapping("/cancion/{id}")
    //@Override
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        cancionService.eliminarCancion(id);
        return ResponseEntity.noContent().build();
    }
}
