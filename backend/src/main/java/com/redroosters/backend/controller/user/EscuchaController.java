package com.redroosters.backend.controller.user;

import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Escucha;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.repository.EscuchaRepository;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class EscuchaController {

    private final EscuchaRepository escuchaRepository;
    private final CancionRepository cancionRepository;

    public EscuchaController(EscuchaRepository escuchaRepository, CancionRepository cancionRepository) {
        this.escuchaRepository = escuchaRepository;
        this.cancionRepository = cancionRepository;
    }

    // Registra las escuchas de una cancion por usuario

    @PostMapping("/escuchar/{idCancion}")
    public ResponseEntity<EscuchaResponseDTO> registrarEscucha(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long idCancion) {

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new CancionNotFoundException(idCancion));

        Escucha escucha = escuchaRepository.findByUsuarioIdAndCancionId(usuario.getId(), cancion.getId())
                .orElse(null);

        if (escucha == null) {
            escucha = new Escucha();
            escucha.setUsuario(usuario);
            escucha.setCancion(cancion);
            escucha.setVecesEscuchada(0);
        }

        escucha.setVecesEscuchada(escucha.getVecesEscuchada() + 1);
        escucha.setUltimaEscucha(LocalDateTime.now());

        Escucha guardada = escuchaRepository.save(escucha);

        EscuchaResponseDTO dto = new EscuchaResponseDTO(
                guardada.getId(),
                usuario.getName(),
                cancion.getTitulo(),
                guardada.getVecesEscuchada(),
                guardada.getUltimaEscucha()
        );

        return ResponseEntity.ok(dto);
    }
}

