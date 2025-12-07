package com.redroosters.backend.service;

import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.dto.TopCancionDTO;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.exception.UsuarioNotFoundException;
import com.redroosters.backend.mapper.EscuchaMapper;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Escucha;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.repository.EscuchaRepository;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

// Logica para contar y visualizar la Escucha de una cancion

@Service
public class EscuchaService {

    private final EscuchaRepository escuchaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private final EscuchaMapper escuchaMapper;

    public EscuchaService(EscuchaRepository escuchaRepository,
                          UsuarioRepository usuarioRepository,
                          CancionRepository cancionRepository,
                          EscuchaMapper escuchaMapper) {
        this.escuchaRepository = escuchaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
        this.escuchaMapper = escuchaMapper;
    }

    @Transactional
    public EscuchaResponseDTO registrarEscucha(Long usuarioId, Long cancionId) {

        // Miramos si ya existe una escucha de ese usuario para esa cancion
        Escucha escucha = escuchaRepository
                .findByUsuarioIdAndCancionId(usuarioId, cancionId)
                .orElse(null);

        if (escucha != null) {
            // Ya la ha escuchado antes -> NO incrementamos
            // Solo actualizamos la fecha de ultima escucha
            escucha.setUltimaEscucha(LocalDateTime.now());

        } else {
            // Primera vez que este usuario escucha esta cancion
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(usuarioId)));

            Cancion cancion = cancionRepository.findById(cancionId)
                    .orElseThrow(() -> new CancionNotFoundException(cancionId));

            escucha = new Escucha();
            escucha.setUsuario(usuario);
            escucha.setCancion(cancion);
            escucha.setVecesEscuchada(1L);                 // SIEMPRE 1
            escucha.setUltimaEscucha(LocalDateTime.now());
        }

        Escucha guardada = escuchaRepository.save(escucha);
        return escuchaMapper.toDto(guardada);
    }



    // Contador global (publico)
    @Transactional(readOnly = true)
    public Long contadorGlobalCancion(Long cancionId) {
        cancionRepository.findById(cancionId)
                .orElseThrow(() -> new CancionNotFoundException(cancionId));
        return escuchaRepository.sumaGlobalPorCancion(cancionId);
    }

    // Top global (publico)
    @Transactional(readOnly = true)
    public List<TopCancionDTO> topCancionesMasEscuchadas(int limit) {

        var ranking = escuchaRepository.topGlobal(PageRequest.of(0, limit));
        var ids = ranking.stream().map(r -> r.getCancionId()).toList();
        var canciones = cancionRepository.findAllById(ids);

        // Mapa idCancion → Cancion para componer rápido
        Map<Long, Cancion> porId = new HashMap<>();
        for (Cancion c : canciones) porId.put(c.getId(), c);

        List<TopCancionDTO> resultado = new ArrayList<>();
        for (var r : ranking) {
            Cancion c = porId.get(r.getCancionId());
            if (c != null) {
                resultado.add(escuchaMapper.toTopDto(c, r.getTotal()));
            }
        }
        return resultado;
    }
}
