package com.redroosters.backend.controller.user;


import com.redroosters.backend.docs.user.LikeUserApi;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.dto.LikedResponse;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.service.LikeService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class LikeController implements LikeUserApi {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }


    //Crear like para una cancion.
    @PostMapping("/like/{cancionId}")
    @Override
    public ResponseEntity<Void> darLike(@PathVariable Long cancionId,
                                        @RequestParam(required = false) String nombre,
                                        @RequestParam(required = false) String titulo,
                                        @AuthenticationPrincipal Usuario usuario) {
        likeService.guardarLike(usuario.getId(), cancionId, nombre, titulo);
        return ResponseEntity.noContent().build();
    }

    // Eliminar like de una cancion.
    @DeleteMapping("/like/{cancionId}")
    @Override
    public ResponseEntity<Void> quitarLike(@PathVariable Long cancionId,
                                           @RequestParam(required = false) String titulo,
                                           @AuthenticationPrincipal Usuario usuario) {
        likeService.eliminarLike(usuario.getId(), cancionId, titulo);
        return ResponseEntity.noContent().build(); // 204 siempre, sea que existiera o no
    }


     // Comprobar si el usuario actual ha dado like a una cancion.
     // Devuelve: true/false
    @GetMapping("/like/{cancionId}/exists")
    @Override
    public ResponseEntity<LikedResponse> existeLike(@PathVariable Long cancionId,
                                                    @AuthenticationPrincipal Usuario usuario) {
        boolean liked = likeService.existeLike(usuario.getId(), cancionId);
        return ResponseEntity.ok(new LikedResponse(liked));
    }

    // Mostra en la web todas las canciones favoritas de un Usuario (Paginado)
    @GetMapping("/likes")
    @Override
    public ResponseEntity<Page<CancionResponseDTO>> listarFavoritos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "titulo") String sort,
            @AuthenticationPrincipal Usuario usuario) {

        Page<CancionResponseDTO> favoritos =
                likeService.listarFavoritos(usuario.getId(), page, size, sort);

        return ResponseEntity.ok(favoritos);
    }


}


