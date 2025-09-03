package com.redroosters.backend.controller.admin;

import com.redroosters.backend.docs.admin.AlbumAdminApi;
import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Crear, Editar, Eliminar Album

@RestController
@RequestMapping("/api/admin")
public class AlbumController implements AlbumAdminApi {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }


    @PostMapping("/album")
    @Override
    public ResponseEntity<AlbumResponseDTO> crearAlbum(@RequestBody @Valid AlbumRequestDTO dto) {
        AlbumResponseDTO creado = albumService.crearAlbum(dto);
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
