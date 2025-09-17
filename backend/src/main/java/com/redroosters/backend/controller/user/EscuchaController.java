package com.redroosters.backend.controller.user;

import com.redroosters.backend.docs.user.EscuchaUserApi;
import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.service.EscuchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class EscuchaController implements EscuchaUserApi {

    private final EscuchaService escuchaService;

    public EscuchaController(EscuchaService escuchaService) {
        this.escuchaService = escuchaService;
    }


    // Registrar escucha de una cancion (usuario autenticado)
    @PostMapping("/escuchar/{idCancion}")
    public ResponseEntity<EscuchaResponseDTO> registrarEscucha(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long idCancion) {

        if (usuario == null) return ResponseEntity.status(401).build();

        EscuchaResponseDTO dto = escuchaService.registrarEscucha(usuario.getId(), idCancion);
        return ResponseEntity.ok(dto);
    }

}

