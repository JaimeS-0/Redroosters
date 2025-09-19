package com.redroosters.backend.controller.publico;

import com.redroosters.backend.docs.publico.MensajeContactoPublicApi;
import com.redroosters.backend.dto.MensajeContactoRequestDTO;
import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import com.redroosters.backend.service.MensajeContactoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Crear el mensaje en la web para el productor

@RestController
@RequestMapping("/api/public")
public class MensajeContactoController implements MensajeContactoPublicApi {

    private final MensajeContactoService mensajeContactoService;

    public MensajeContactoController(MensajeContactoService mensajeContactoService) {
        this.mensajeContactoService = mensajeContactoService;
    }

    // Crear Mensaje
    @PostMapping("/contacto")
    @Override
    public ResponseEntity<MensajeContactoResponseDTO> crearMensaje(
            @Valid @RequestBody MensajeContactoRequestDTO dto
    ) {
        MensajeContactoResponseDTO res = mensajeContactoService.crearMensaje(dto);
        return ResponseEntity.ok(res);
    }
}
