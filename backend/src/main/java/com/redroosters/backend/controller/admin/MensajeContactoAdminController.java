package com.redroosters.backend.controller.admin;

import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import com.redroosters.backend.service.MensajeContactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Ver mensajes que pone los usuarios al admin

@RestController
@RequestMapping("/api/admin")
public class MensajeContactoAdminController {

    private final MensajeContactoService mensajeContactoService;

    public MensajeContactoAdminController(MensajeContactoService mensajeContactoService) {
        this.mensajeContactoService = mensajeContactoService;
    }

    // Ver Mensajes
    @GetMapping("/VerMensajes")
    @Override
    public ResponseEntity<List<MensajeContactoResponseDTO>> verMensaje(){
        List<MensajeContactoResponseDTO> mensajes = mensajeContactoService.listarMensajes();
        return ResponseEntity.ok(mensajes);
    }

    // GET /VerMensajes/{id}
    @GetMapping("/VerMensajes/{id}")
    @Override
    public ResponseEntity<MensajeContactoResponseDTO> verMensajeId(@PathVariable Long id) {
        MensajeContactoResponseDTO dto = mensajeContactoService.getMensajeId(id);
        return ResponseEntity.ok(dto);
    }

}
