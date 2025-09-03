package com.redroosters.backend.service;

import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.mapper.ArtistaMapper;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.repository.ArtistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ArtistaResponseDTO> listarTodos() {

        // Obtenemos todos los artistas
        List<Artista> artistas =artistaRepository.findAll();

        // Convertimos la lista de entidades a DTOs
        return artistaMapper.toDToList(artistas);
    }

    // Publico USER -> Mostra informacion de cada artista
    public ArtistaResponseDTO getPorId(Long id, String titulo) {

        // Buscar artista por Id
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(titulo));

        // Convertirlo a DTO y devolverlo
        return artistaMapper.toDTO(artista);
    }

    // Privado ADMIN
    public ArtistaResponseDTO editar(Long id, ArtistaRequestDTO dto) {

        // Buscar artista para editar
        Artista existente = artistaRepository.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(id));

        // Actualizar los campos
        existente.setNombre(dto.nombre());
        existente.setDescripcion(dto.descripcion());
        existente.setPortadaUrl(dto.portadaUrl());
        existente.setDestacado(dto.destacado());

        // Guardar cambios
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
