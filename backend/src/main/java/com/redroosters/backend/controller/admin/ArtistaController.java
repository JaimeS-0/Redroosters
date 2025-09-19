package com.redroosters.backend.controller.admin;

import com.redroosters.backend.docs.admin.ArtistaAdminApi;
import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.service.ArtistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Crear, Editar, Eliminar Artista

@RestController
@RequestMapping("/api/admin")
public class ArtistaController implements ArtistaAdminApi {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
    }

    // Crear artista
    @PostMapping("/artistas")
    @Override
    public ResponseEntity<ArtistaResponseDTO> crearArtista(@RequestBody @Valid ArtistaRequestDTO dto) {
        ArtistaResponseDTO creado = artistaService.crearArtista(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Editar artista
    @PutMapping("/artistas/{id}")
    @Override
    public ResponseEntity<ArtistaResponseDTO> editarArtista(
            @PathVariable Long id,
            @RequestBody @Valid ArtistaRequestDTO dto
    ) {
        return ResponseEntity.ok(artistaService.editar(id, dto));
    }

    // Eliminar artista
    @DeleteMapping("/artistas/{id}")
    @Override
    public ResponseEntity<Void> eliminarArtista(@PathVariable Long id) {
        artistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
