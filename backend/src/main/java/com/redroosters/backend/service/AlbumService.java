package com.redroosters.backend.service;

import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.exception.AlbumNotFoundException;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.mapper.AlbumMapper;
import com.redroosters.backend.model.Album;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.repository.AlbumRepository;
import com.redroosters.backend.repository.ArtistaRepository;
import com.redroosters.backend.repository.CancionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // CRUD

    // Privado ADMIN
    public AlbumResponseDTO crearAlbum(AlbumRequestDTO dto) {

        // Buscar el artista
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        // Busca canciones por el Id
        //List<Cancion> canciones = cancionRepository.findAllById(dto.cancionesIds());

        // Mapear el DTO a la entidad Album
        Album album = albumMapper.toEntity(dto);

        // Asignar relaciones de album
        album.setArtista(artista);


        // Manejo seguro de canciones
        List<Long> ids = dto.cancionesIds() == null ? List.of() : dto.cancionesIds();
        List<Cancion> canciones = ids.isEmpty() ? List.of() : cancionRepository.findAllById(ids);

        if (canciones.size() != ids.size()) {
            throw new CancionNotFoundException("La cancion no existe: " + ids);
        }

        // Asignar relaciones de canciones
        album.setCanciones(canciones);

        Album guardado = albumRepository.save(album);
        return albumMapper.toDto(guardado);
    }


    // Privado admin
    public AlbumResponseDTO editarAlbum(Long id, AlbumRequestDTO dto) {

        // Buscar el album exsitente
        Album existente = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(id));

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
    public void eliminarAlbum(Long id) {

        // Verificamos si el album existe
        if (!albumRepository.existsById(id)) {
            throw new AlbumNotFoundException(id);
        }

        // Eliminamos por Id
        albumRepository.deleteById(id);
    }

    // Listar los albumes

    // publico USER con paginación
    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> listarAlbumes(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return albumRepository.findAll(pageable)
                .map(albumMapper::toDto);
    }


    // publico USER mostrar detalles
    @Transactional(readOnly = true)
    public AlbumResponseDTO obtenerAlbumPorId(Long id) {

        // Buscar album por Id
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(id));

        // Tranforma a DTO y devolcer
        return albumMapper.toDto(album);
    }


    // Consultas Personalizadas del repositorio

    // Publico USER → Listar por artista (con paginación)
    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> listarAlbumesPorArtista(Long artistaId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return albumRepository.findByArtistaId(artistaId, pageable).map(albumMapper::toDto);
    }
}
