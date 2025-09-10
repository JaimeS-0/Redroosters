package com.redroosters.backend.controller.publico;

import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.service.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ver Albumes en la pagina

@RestController
@RequestMapping("/api/public")
public class AlbumPublicController {

    private final AlbumService albumService;

    public AlbumPublicController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // Listar todos los Albumes con paginacion
    @GetMapping("/album")
    public ResponseEntity<Page<AlbumResponseDTO>> listarAlbumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        Page<AlbumResponseDTO> albumes = albumService.listarAlbumes(page,size, sort);
        return ResponseEntity.ok(albumes);
    }

    // Ver detalle de un album
    @GetMapping("/album/{id}")
    public ResponseEntity<AlbumResponseDTO> verDetalle(@PathVariable Long id) {
        AlbumResponseDTO album = albumService.obtenerAlbumPorId(id);
        return ResponseEntity.ok(album);
    }

    // Listar álbumes por artista
    @GetMapping("/album/artista/{idArtista}")
    public ResponseEntity<List<AlbumResponseDTO>> listarPorArtista(@PathVariable Long idArtista) {
        List<AlbumResponseDTO> albumes = albumService.listarAlbumesPorArtista(idArtista);
        return ResponseEntity.ok(albumes);
    }


}
