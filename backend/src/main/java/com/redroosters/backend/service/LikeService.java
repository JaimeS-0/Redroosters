package com.redroosters.backend.service;

import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.dto.LikeResponseDTO;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.exception.LikeNotFoundException;
import com.redroosters.backend.exception.LikeAlreadyExistsException;
import com.redroosters.backend.exception.UsuarioNotFoundException;
import com.redroosters.backend.mapper.CancionMapper;
import com.redroosters.backend.mapper.LikeMapper;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Like;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.repository.LikeRepository;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Logica de CRUD y visualizar los likes de las canciones

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private final CancionMapper cancionMapper;
    private final LikeMapper likeMapper;

    public LikeService(LikeRepository likeRepository, UsuarioRepository usuarioRepository,
                      CancionRepository cancionRepository, CancionMapper cancionMapper, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
        this.cancionMapper = cancionMapper;
        this.likeMapper = likeMapper;
    }

    public void guardarLike(Long usuarioId, Long cancionId, String nombre, String titulo) {

        // Verificamos si ya exite el like
        if (likeRepository.existsByUsuarioIdAndCancionId(usuarioId, cancionId)){
            throw new LikeAlreadyExistsException();
        }

        // Buscamos el usuario y la canción
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(nombre));

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new CancionNotFoundException(titulo));

        // Creamos y guardamos el like
        Like nuevoLike = new Like();
        nuevoLike.setUsuario(usuario);
        nuevoLike.setCancion(cancion);

        likeRepository.save(nuevoLike);
    }

    @Transactional
    public void eliminarLike(Long usuarioId, Long cancionId, String titulo) {
        if (!likeRepository.existsByUsuarioIdAndCancionId(usuarioId, cancionId)) {
            throw new LikeNotFoundException(titulo); // Excepcion si se intenta eliminar algo que no existe
        }

        likeRepository.deleteByUsuarioIdAndCancionId(usuarioId, cancionId);
    }

    public Page<CancionResponseDTO> listarFavoritos(Long userId, int page, int size, String sort) {
        String sortProperty = switch (sort) {
            case "titulo" -> "cancion.titulo";
            default -> sort;
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());

        return likeRepository.findByUsuarioId(userId, pageable)
                .map(like -> cancionMapper.toDto(like.getCancion()));
    }


    // Comprueba si ya existe un like para esa canción y usuario
    public boolean existeLike(Long usuarioId, Long cancionId) {
        return likeRepository.existsByUsuarioIdAndCancionId(usuarioId, cancionId);
    }

    // Contar todos los likes
    public long contarLikes(Long cancionId) {
        return likeRepository.countByCancionId(cancionId);
    }




}
