package com.redroosters.backend.controller.admin;

import com.redroosters.backend.docs.admin.CancionAdminApi;
import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.CancionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Crear, Editar, Eliminar Canciones

@RestController
@RequestMapping("/api/admin")
public class CancionController implements CancionAdminApi {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    // Crear Canciones
    @PostMapping("/cancion")
    @Override
    public ResponseEntity<CancionResponseDTO> crearCancion(@Valid @RequestBody CancionRequestDTO dto) {
        CancionResponseDTO creado = cancionService.crearCancion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Editar Canciones
    @PutMapping("/canciones/{id}")
    @Override
    public ResponseEntity<CancionResponseDTO> editarCancion(
            @PathVariable Long id,
            @PathVariable CancionRequestDTO dto
    ){
        return ResponseEntity.ok(cancionService.editarCancion(id, dto));
    }

    // Eliminar cancion
    @DeleteMapping("/canciones/{id}")
    @Override
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        cancionService.eliminarCancion(id);
        return ResponseEntity.noContent().build();

    }

}
