package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.CancionPublicApi;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.CancionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Mostrar las Canciones en la Web

@RestController
@RequestMapping("/api/public")
public class CancionPublicController implements CancionPublicApi {

    private final CancionService cancionService;

    public CancionPublicController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    // Ver todas las canciones
    @GetMapping("/cancion")
    @Override
    public ResponseEntity<Page<CancionResponseDTO>> listarCanciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        Page<CancionResponseDTO> canciones = cancionService.listarCanciones(page, size, sort);
        return ResponseEntity.ok(canciones);
    }

    //  Listar canciones por álbum (paginado)
    @GetMapping("/album/{albumId}/cancion")
    @Override
    public ResponseEntity<Page<CancionResponseDTO>> listarCancionesPorAlbum(
            @PathVariable Long albumId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        Page<CancionResponseDTO> canciones = cancionService.listarCancionesPorAlbum(albumId, page, size, sort);
        return ResponseEntity.ok(canciones);
    }

    // Listar singles de un artista (canciones sin álbum)
    @GetMapping("/artista/{artistaId}/singles")
    @Override
    public ResponseEntity<Page<CancionResponseDTO>> listarSinglesPorArtista(
            @PathVariable Long artistaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        Page<CancionResponseDTO> canciones = cancionService.listarSinglesPorArtista(artistaId, page, size, sort);
        return ResponseEntity.ok(canciones);
    }

    // Ver detalle de una canción
    @GetMapping("/cancion/{id}")
    @Override
    public ResponseEntity<CancionResponseDTO> verDetalle(@PathVariable Long id) {
        CancionResponseDTO dto = cancionService.verDetalleCancion(id);
        return ResponseEntity.ok(dto);
    }
}

