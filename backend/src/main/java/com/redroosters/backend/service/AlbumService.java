package com.redroosters.backend.service;

import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.exception.AlbumNotFoundException;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.mapper.AlbumMapper;
import com.redroosters.backend.model.Album;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.repository.AlbumRepository;
import com.redroosters.backend.repository.ArtistaRepository;
import com.redroosters.backend.repository.CancionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final CancionRepository cancionRepository;
    private final AlbumMapper albumMapper;

    public AlbumService(AlbumRepository albumRepository,
                        ArtistaRepository artistaRepository,
                        CancionRepository cancionRepository,
                        AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.cancionRepository = cancionRepository;
        this.albumMapper = albumMapper;
    }

    // Privado ADMIN
    public Album crearAlbum(AlbumRequestDTO dto) {

        // Buscar el artista
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        // Busca canciones por el Id
        List<Cancion> cancions = cancionRepository.findAllById(dto.cancionesIds());

        // Mapear el DTO a la entidad Album
        Album album = albumMapper.toEntity(dto);

        // Asignar relaciones
        album.setArtista(artista);
        album.setCanciones(cancions);

        return albumRepository.save(album);
    }

    // publico USER
    public List<AlbumResponseDTO> listarAlbum() {

        // Devuelve todos los albumes
        List<Album> albumes = albumRepository.findAll();
        return albumMapper.toDtoList(albumes);
    }

    // publico USER mostrar detalles
    public AlbumResponseDTO getAlbumPorId(Long id, String titulo) {

        // Buscar album por Id
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(titulo));

        // Tranforma a DTO y devolcer
        return albumMapper.toDto(album);
    }

    // Privado admin
    public AlbumResponseDTO editarAlbum(Long id,String titulo, AlbumRequestDTO dto) {

        // Buscar el album exsitente
        Album existente = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(id, titulo));

        // Actualizar los campos editables
        existente.setTitulo(dto.titulo());
        existente.setDescripcion(dto.descripcion());
        existente.setPortadaUrl(dto.portadaUrl());

        // Cambiar artista
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));
        existente.setArtista(artista);

        // Cambiar canciones
        List<Cancion> canciones = cancionRepository.findAllById(dto.cancionesIds());
        existente.setCanciones(canciones);

        Album actualizado = albumRepository.save(existente);
        return albumMapper.toDto(actualizado);
    }

    // Privado admin
    public void eliminarAlbum(Long id, String titulo) {

        // Verificamos si el album existe
        if (!albumRepository.existsById(id)) {
            throw new AlbumNotFoundException(id, titulo);
        }

        // Eliminamos por Id
        albumRepository.deleteById(id);
    }

    // Consultas Personalizadas del repositorio

    public List<AlbumResponseDTO> listarAlbumesPorArtista(Long artistaId) {

        // Busca todos los albumes de un artista
        List<Album> albumes = albumRepository.findByArtistaId(artistaId);

        // Tranformar a DTO
        return albumMapper.toDtoList(albumes);
    }

}
