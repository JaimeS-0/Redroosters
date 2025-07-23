package com.redroosters.backend.service;

import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.exception.UsuarioNotFoundException;
import com.redroosters.backend.mapper.EscuchaMapper;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Escucha;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.repository.EscuchaRepository;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    public void registrarEscucha(Long usuarioId, Long cancionId, String nombre, String titulo) {

        // Comprobamos si existe la escucha
        Optional<Escucha> existente = escuchaRepository.findByUsuarioIdAndCancionId(usuarioId, cancionId);

        if (existente.isPresent()) {

            // Si ya exixte, actualizamos el contador y fecha
            Escucha escucha = existente.get();
            escucha.setVecesEscuchada(escucha.getVecesEscuchada() + 1);
            escucha.setUltimaEscucha(LocalDateTime.now());
            escuchaRepository.save(escucha);
        } else {

            // Si no exite crear nueva escucha, Buscamos usuario y cancion y añadimos
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioNotFoundException(nombre));

            Cancion cancion = cancionRepository.findById(cancionId)
                    .orElseThrow(() -> new CancionNotFoundException(titulo));

            Escucha nueva = new Escucha();
            nueva.setUsuario(usuario);
            nueva.setCancion(cancion);
            nueva.setVecesEscuchada(1);
            nueva.setUltimaEscucha(LocalDateTime.now());

            escuchaRepository.save(nueva);
        }
    }

    public List<EscuchaResponseDTO> listarEscuchaPorUsuario(Long usuarioId, String nombre) {

        // Validar que el usuariio existe
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNotFoundException(nombre);
        }

        // Obtenemos todas las escuchas de ese usuasrio
        List<Escucha> escuchas = escuchaRepository.findByUsuarioId(usuarioId);

        return escuchaMapper.toDtoList(escuchas);
    }

    public List<EscuchaResponseDTO> topCancionesMasEscuchadas() {
        List<Escucha> top = escuchaRepository.findTop10ByOrderByVecesEscuchadaDesc();
        return escuchaMapper.toDtoList(top);
    }
}
