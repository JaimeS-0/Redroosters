package com.redroosters.backend.controller.admin;

import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Crear Album

@RestController
@RequestMapping("/api/admin")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // @Override
    @PostMapping("/album")
    public ResponseEntity<AlbumResponseDTO> crearAlbum(@RequestBody @Valid AlbumRequestDTO dto) {
        AlbumResponseDTO creado = albumService.crearAlbum(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // @Override
    @PutMapping("/album/{id}")
    public ResponseEntity<AlbumResponseDTO> editarAlbum(
            @PathVariable Long id,
            @RequestBody @Valid AlbumRequestDTO dto
    ) {
        return ResponseEntity.ok(albumService.editarAlbum(id, dto));
    }

    // @Override
    @DeleteMapping("/album/{id}")
    public ResponseEntity<Void> eliminarAlbum(@PathVariable Long id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.noContent().build();
    }

}
