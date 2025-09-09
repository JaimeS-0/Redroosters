package com.redroosters.backend.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redroosters.backend.docs.admin.CancionAdminApi;
import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.CancionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

// Crear, Editar, Eliminar Canciones

@RestController
@RequestMapping("/api/admin")
public class CancionController implements CancionAdminApi {

    private final CancionService cancionService;
    private final ObjectMapper objectMapper;

    public CancionController(CancionService cancionService, ObjectMapper objectMapper) {
        this.cancionService = cancionService;
        this.objectMapper = objectMapper;
    }

    // Crear CON archivo (multipart) .mp3
    @PostMapping(value = "/cancion/archivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CancionResponseDTO> crearConArchivo(
            @RequestPart("datos") String datosJson,
            @RequestPart("audio") MultipartFile audio
    ) {
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

        var resp = cancionService.crearConArchivo(dto, audio);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // Editar SIN archivo
    @PutMapping("/cancion/{id}")
    public ResponseEntity<CancionResponseDTO> editarCancion(
            @PathVariable Long id,
            @RequestBody CancionRequestDTO dto) {
        return ResponseEntity.ok(cancionService.editarCancion(id, dto));
    }

    // Eliminar
    @DeleteMapping("/cancion/{id}")
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        cancionService.eliminarCancion(id);
        return ResponseEntity.noContent().build();
    }
}
