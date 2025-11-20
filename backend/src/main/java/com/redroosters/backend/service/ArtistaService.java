package com.redroosters.backend.service;

import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.mapper.ArtistaMapper;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

// Logica de CRUD y visualizar Artistas

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ArtistaMapper artistaMapper;

    public ArtistaService(ArtistaRepository artistaRepository, ArtistaMapper artistaMapper) {
        this.artistaRepository = artistaRepository;
        this.artistaMapper = artistaMapper;
    }

    // Privado ADMIN
    public ArtistaResponseDTO crearArtista(ArtistaRequestDTO dto) {

        // Convertimos el DTO a entidad Artista
        Artista artista = artistaMapper.toEntity(dto);

        // Guardamos el artista en la BBDD
        Artista guardado = artistaRepository.save(artista);

        // Lo tranformamos en DTO y lo devolvemos
        return artistaMapper.toDTO(guardado);
    }

    // Publico USER
    // Listar todos los artistas con paginacion
    public Page<ArtistaResponseDTO> listarTodos(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        return artistaRepository.findAll(pageable)
                .map(artista -> new ArtistaResponseDTO(
                        artista.getId(),
                        artista.getNombre(),
                        artista.getUrlNombre(),
                        artista.getDescripcion(),
                        artista.getPortadaUrl(),
                        artista.isDestacado()
                ));
    }

    // Publico USER
    // Listar solo 6 artistas pagina de artistas
    public List<ArtistaResponseDTO> randomArtistas(int size, List<Long> excludeIds) {
        if (excludeIds == null) excludeIds = List.of();
        return artistaRepository.findRandom(size, excludeIds)
                .stream()
                .map(artistaMapper::toDTO)
                .toList();
    }

    // Publico USER
    // Muestra informacion de un artista
    public ArtistaResponseDTO getId(Long id) {
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(id));
        return artistaMapper.toDTO(artista);
    }

    // Privado ADMIN
    public ArtistaResponseDTO editar(Long id, ArtistaRequestDTO dto) {

        Artista existente = artistaRepository.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(id));

        // Solo cambiar nombre si viene
        if (dto.nombre() != null && !dto.nombre().isBlank()) {
            existente.setNombre(dto.nombre());

            // Si tambien viene urlNombre, lo actualizamos
            if (dto.urlNombre() != null && !dto.urlNombre().isBlank()) {
                existente.setUrlNombre(dto.urlNombre());
            }
        }

        // Solo cambiar descripcion si viene
        if (dto.descripcion() != null) {
            existente.setDescripcion(dto.descripcion());
        }

        // Solo cambiar portada si viene una nueva URL
        if (dto.portadaUrl() != null) {
            existente.setPortadaUrl(dto.portadaUrl());
        }

        // Destacado lo actualizamos siempre (el front siempre lo manda)
        existente.setDestacado(dto.destacado());

        Artista actualizado = artistaRepository.save(existente);
        return artistaMapper.toDTO(actualizado);
    }


    // Privado ADMIN
    public void eliminar(Long id) {

        // Verificamos si existe
        if (!artistaRepository.existsById(id)) {
            throw new ArtistaNotFoundException(id);
        }

        artistaRepository.deleteById(id);
    }

    // Metodos del repositorio

    // Privado ADMIN o USER ?
    public List<ArtistaResponseDTO> listarDestacados() {

        // Buscamos todos los artistas que tienen destacado true
        List<Artista> destacados = artistaRepository.findByDestacadoTrue();

        // Converti DTO y se devuelve
        return artistaMapper.toDToList(destacados);
    }




}
