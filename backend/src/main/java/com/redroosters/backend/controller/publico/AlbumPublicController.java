package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.AlbumPublicApi;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.service.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Ver Albumes en la pagina

@RestController
@RequestMapping("/api/public")
public class AlbumPublicController implements AlbumPublicApi {

    private final AlbumService albumService;

    public AlbumPublicController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // Listar todos los Albumes con paginacion
    @GetMapping("/album")
    @Override
    public ResponseEntity<Page<AlbumResponseDTO>> listarAlbumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        Page<AlbumResponseDTO> albumes = albumService.listarAlbumes(page,size, sort);
        return ResponseEntity.ok(albumes);
    }

    // Ver detalle de un album
    @GetMapping("/album/{id}")
    @Override
    public ResponseEntity<AlbumResponseDTO> verDetalle(@PathVariable Long id) {
        AlbumResponseDTO album = albumService.obtenerAlbumPorId(id);
        return ResponseEntity.ok(album);
    }

}
