package com.redroosters.backend.service;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.mapper.CancionMapper;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.repository.ArtistaRepository;
import com.redroosters.backend.repository.CancionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancionService {

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final CancionMapper cancionMapper;

    public CancionService(CancionRepository cancionRepository, ArtistaRepository artistaRepository, CancionMapper cancionMapper) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.cancionMapper = cancionMapper;
    }

    // Privado ADMIN
    public CancionResponseDTO crearCancion(CancionRequestDTO dto) {

        // Buscamar artista
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        // Convertimos DTO en entidad
        Cancion cancion = cancionMapper.toEntity(dto);

        // Asignar artita a la cancion
        cancion.setArtista(artista);

        Cancion guardada = cancionRepository.save(cancion);

        // Devolvemos DTO
        return cancionMapper.toDto(guardada);
    }

    // Publico USER
    public List<CancionResponseDTO> listarCanciones() {

        List<Cancion> canciones = cancionRepository.findAll();

        return cancionMapper.toDtoList(canciones);
    }

    // Privado ADMIN
    public CancionResponseDTO editarCancion(Long id, CancionRequestDTO dto) {

        // Buscamos la cancion
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new CancionNotFoundException(id));

        // Buscamos el nuevo artista
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        // Actualizar campos
        cancion.setTitulo(dto.titulo());
        cancion.setDescripcion(dto.descripcion());
        cancion.setDuracion(dto.duracion());
        cancion.setPortada(dto.portadaUrl());
        cancion.setUrlAudio(dto.urlAudio());
        cancion.setArtista(artista);

        // Guardamos y devolvemos
        Cancion actualizada = cancionRepository.save(cancion);
        return cancionMapper.toDto(actualizada);
    }

    // Privado ADMIN
    public void eliminarCancion(Long id) {

        if (!cancionRepository.existsById(id)) {
            throw new CancionNotFoundException(id);
        }

        cancionRepository.deleteById(id);
    }

    // Metodos personalizados del repositorio

    // Devuelve todas las canciones que pertenecen a un artista concreto, sin importar si tienen álbum o no.
    // Publico USER
    public List<CancionResponseDTO> listarPorArtista(Long artistaId) {
        List<Cancion> canciones = cancionRepository.findByArtistaId(artistaId);
        return cancionMapper.toDtoList(canciones);
    }

    // Devuelve todas las canciones que están asociadas a un álbum concreto.
    // Publico USER
    public List<CancionResponseDTO> listarPorAlbum(Long albumId) {
        List<Cancion> canciones = cancionRepository.findByAlbumId(albumId);
        return cancionMapper.toDtoList(canciones);
    }

    // Devuelve todas las canciones del artista que no tienen álbum asignado.
    // Cuando este creando o editando un album muestra las canciones "libres"
    // Privado ADMIN
    public List<CancionResponseDTO> listarCancionesSinAlbum(Long artistaId) {
        List<Cancion> canciones = cancionRepository.findByArtistaIdAndAlbumIsNull(artistaId);
        return cancionMapper.toDtoList(canciones);
    }

}
