package com.redroosters.backend.controller.publico;

import com.redroosters.backend.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Este controller cuenta el total de likes de una cancion.

@RestController
@RequestMapping("/api/public/")
public class LikePublicController {

    private final LikeService likeService;

    public LikePublicController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/like/{cancionId}/count")
    public ResponseEntity<Long> count(@PathVariable Long cancionId) {
        return ResponseEntity.ok(likeService.contarLikes(cancionId));
    }
}

