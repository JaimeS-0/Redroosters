package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.EscuchaPublicApi;
import com.redroosters.backend.dto.TopCancionDTO;
import com.redroosters.backend.service.EscuchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ver escuchas totales de la cancion y el top de canciones mas escuchadas

@RestController
@RequestMapping("/api/public")
public class EscuchaPublicController implements EscuchaPublicApi {

    private final EscuchaService escuchaService;

    public EscuchaPublicController(EscuchaService escuchaService) {
        this.escuchaService = escuchaService;
    }

    // Contador global de una canción
    @GetMapping("/cancion/{id}/escuchas")
    @Override
    public ResponseEntity<Long> contadorGlobal(@PathVariable Long id) {
        return ResponseEntity.ok(escuchaService.contadorGlobalCancion(id));
    }

    // TOP global
    @GetMapping("/cancion/top-escuchadas")
    @Override
    public ResponseEntity<List<TopCancionDTO>> top(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(escuchaService.topCancionesMasEscuchadas(limit));
    }
}
