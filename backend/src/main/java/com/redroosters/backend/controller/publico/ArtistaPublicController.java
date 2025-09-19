package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.ArtistaPublicApi;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.AlbumService;
import com.redroosters.backend.service.ArtistaService;
import com.redroosters.backend.service.CancionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ver Artistas en la web, sus albumes y canciones

@RestController
@RequestMapping("/api/public")
public class ArtistaPublicController implements ArtistaPublicApi {

    private final ArtistaService artistaService;
    private final AlbumService albumService;
    private final CancionService cancionService;

    public ArtistaPublicController(ArtistaService artistaService, AlbumService albumService, CancionService cancionService) {
        this.artistaService = artistaService;
        this.albumService = albumService;
        this.cancionService = cancionService;
    }

    // Listar Todos los artista paginados (En principio no se usa, se usa el de abajo)
    @GetMapping("/artistas")
    @Override
    public ResponseEntity<Page<ArtistaResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "nombre") String sort
    ) {
        Page<ArtistaResponseDTO> artistas = artistaService.listarTodos(page, size, sort);
        return ResponseEntity.ok(artistas);
    }

    // Listar pagina de artistas, muestra solo 6 artistas
    @GetMapping("/artistas/random")
    @Override
    public ResponseEntity<List<ArtistaResponseDTO>> artistasRandom(
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) List<Long> excludeIds
    ) {
        return ResponseEntity.ok(artistaService.randomArtistas(size, excludeIds));
    }


    // Ver los destalles de un artista id
    @GetMapping("/artista/{id}")
    @Override
    public ResponseEntity<ArtistaResponseDTO> verDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(artistaService.getId(id));
    }

    //  Álbumes del artista
    @GetMapping("/artista/{id}/album")
    @Override
    public ResponseEntity<Page<AlbumResponseDTO>> albumesDeArtista(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        return ResponseEntity.ok(albumService.listarAlbumesPorArtista(id, page, size, sort));
    }

    // Canciones del artista
    @GetMapping("/artista/{id}/cancion")
    @Override
    public ResponseEntity<Page<CancionResponseDTO>> cancionesDeArtista(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "titulo") String sort
    ) {
        return ResponseEntity.ok(cancionService.listarCancionesPorArtista(id, page, size, sort));
    }
}
