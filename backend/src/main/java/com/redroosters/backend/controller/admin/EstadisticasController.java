package com.redroosters.backend.controller.admin;

import com.redroosters.backend.docs.admin.EstadisticasAdminApi;
import com.redroosters.backend.dto.EstadisticasDTO;
import com.redroosters.backend.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller para mostrar las estadisitcas en el panel de administracion

@RestController
@RequestMapping("/api/admin")
public class EstadisticasController implements EstadisticasAdminApi {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> getStats() {
        return ResponseEntity.ok(estadisticasService.getStats());
    }
}
