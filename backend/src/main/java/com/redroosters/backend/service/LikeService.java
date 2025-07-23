package com.redroosters.backend.service;

import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.exception.LikeNotFoundException;
import com.redroosters.backend.exception.LikeAlreadyExistsException;
import com.redroosters.backend.exception.UsuarioNotFoundException;
import com.redroosters.backend.mapper.CancionMapper;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Like;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.repository.LikeRepository;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private final CancionMapper cancionMapper;

    public LikeService(LikeRepository likeRepository, UsuarioRepository usuarioRepository,
                      CancionRepository cancionRepository, CancionMapper cancionMapper) {
        this.likeRepository = likeRepository;
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
        this.cancionMapper = cancionMapper;
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

    public void eliminarLike(Long usuarioId, Long cancionId, String titulo) {
        if (!likeRepository.existsByUsuarioIdAndCancionId(usuarioId, cancionId)) {
            throw new LikeNotFoundException(titulo); // Excepcion si se intenta eliminar algo que no existe
        }

        likeRepository.deleteByUsuarioIdAndCancionId(usuarioId, cancionId);
    }

    // Devuelve las canciones que el usuario ha marcado como favoritas
    public List<CancionResponseDTO> listarFavoritos(Long usuarioId) {
        List<Like> likes = likeRepository.findByUsuarioId(usuarioId);

        // Extrae las canciones de los likes
        List<Cancion> canciones = likes.stream()
                .map(Like::getCancion)
                .toList();

        return cancionMapper.toDtoList(canciones);
    }

    // Comprueba si ya existe un like para esa canción y usuario
    public boolean existeLike(Long usuarioId, Long cancionId) {
        return likeRepository.existsByUsuarioIdAndCancionId(usuarioId, cancionId);
    }



}
