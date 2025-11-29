package com.redroosters.backend.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redroosters.backend.docs.admin.AlbumAdminApi;
import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.service.AlbumService;
import jakarta.validation.Valid;
import com.redroosters.backend.service.AlmacenamientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


// Crear, Editar, Eliminar Album

@RestController
@RequestMapping("/api/admin")
public class AlbumController  implements AlbumAdminApi{

    private final AlbumService albumService;
    private final AlmacenamientoService almacenamientoService;
    private final ObjectMapper objectMapper;


    public AlbumController(AlbumService albumService, AlmacenamientoService almacenamientoService, ObjectMapper objectMapper) {
        this.albumService = albumService;
        this.almacenamientoService = almacenamientoService;
        this.objectMapper = objectMapper;

    }


    @PostMapping(
            value = "/album",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Override
    public ResponseEntity<AlbumResponseDTO> crearAlbum(
            @RequestPart("datos") String datosJson,
            @RequestPart(value = "portada", required = false) MultipartFile portada
    ) throws JsonProcessingException {

        // Parsear el JSON a DTO
        AlbumRequestDTO dto = objectMapper.readValue(datosJson, AlbumRequestDTO.class);

        // Guardar portada si viene
        String portadaUrl = null;
        if (portada != null && !portada.isEmpty()) {
            portadaUrl = almacenamientoService.guardarPortada(portada);
        }

        // Reconstruir DTO con la portada (si tu record tiene ese campo)
        AlbumRequestDTO dtoConPortada = new AlbumRequestDTO(
                dto.titulo(),
                dto.descripcion(),
                portadaUrl,
                dto.artistaId(),
                dto.cancionesIds()
        );

        AlbumResponseDTO creado = albumService.crearAlbum(dtoConPortada);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/album/{id}")
    @Override
    public ResponseEntity<AlbumResponseDTO> editarAlbum(
            @PathVariable Long id,
            @RequestBody @Valid AlbumRequestDTO dto
    ) {
        return ResponseEntity.ok(albumService.editarAlbum(id, dto));
    }

    @DeleteMapping("/album/{id}")
    @Override
    public ResponseEntity<Void> eliminarAlbum(@PathVariable Long id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.noContent().build();
    }

}
