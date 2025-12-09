package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.BuscarPublicApi;
import com.redroosters.backend.dto.BusquedaDTO;

import com.redroosters.backend.service.BuscarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Mostrar las busquedas en la web cuando usas el buscador

@RestController
@RequestMapping("/api/public")
public class BuscarPublicController implements BuscarPublicApi {

    private final BuscarService buscarService;

    public BuscarPublicController(BuscarService buscarService) {
        this.buscarService = buscarService;
    }

    @GetMapping("/buscar")
    @Override
    public List<BusquedaDTO> buscar(@RequestParam("q") String q) {
        return buscarService.buscar(q);
    }


    // Endpoint para realizar busquedas
    /*
    @GetMapping("/buscar")
    @Override
    public ResponseEntity<List<BusquedaDTO>> buscar(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(buscarService.buscar(q, page, size));
    }*/
}
