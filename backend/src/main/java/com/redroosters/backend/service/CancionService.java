package com.redroosters.backend.service;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.exception.AlbumNotFoundException;
import com.redroosters.backend.exception.ArtistaNotFoundException;
import com.redroosters.backend.exception.CancionNotFoundException;
import com.redroosters.backend.mapper.CancionMapper;
import com.redroosters.backend.model.Album;
import com.redroosters.backend.model.Artista;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.repository.AlbumRepository;
import com.redroosters.backend.repository.ArtistaRepository;
import com.redroosters.backend.repository.CancionRepository;
import com.redroosters.backend.utils.AudioUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// Logica de CRUD y visualizar Canciones onautomatica de audio

@Service
public class CancionService {

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumRepository albumRepository;
    private final CancionMapper cancionMapper;

    @Value("${app.uploads.audio-dir}")
    private String audioDir;

    public CancionService(CancionRepository cancionRepository,
                          ArtistaRepository artistaRepository,
                          AlbumRepository albumRepository,
                          CancionMapper cancionMapper) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.albumRepository = albumRepository;
        this.cancionMapper = cancionMapper;
    }


     // Crear cancion SUBIENDO archivo. Guarda el fichero en disco, calcula duración con ffprobe,

    @Transactional
    public CancionResponseDTO crearConArchivo(CancionRequestDTO dto, MultipartFile audio) {

        // Validaciones de dominio (404 si no existen)
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        Album album = null;
        if (dto.albumId() != null) {
            album = albumRepository.findById(dto.albumId())
                    .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
        }

        // Validaciones de request (400 si falta algo)
        if (audio == null || audio.isEmpty()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Debes adjuntar el archivo de audio en la parte 'audio'");
        }
        if (audioDir == null || audioDir.isBlank()) {
            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR,
                    "app.uploads.audio-dir no está configurado");
        }

        // Validacion acepta solo archivos .mp3
        String RutaOriginal = audio.getOriginalFilename();

        if (RutaOriginal == null || !RutaOriginal.toLowerCase().endsWith(".mp3")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El archivo debe tener extensión .mp3"
            );
        }


        // manejo específico del archivo
        try {
            Path folder = Paths.get(audioDir).toAbsolutePath().normalize();
            Files.createDirectories(folder);

            String original = (audio.getOriginalFilename() != null) ? audio.getOriginalFilename() : "audio.mp3";
            String safeName = buildSafeFileName(original);
            Path dest = folder.resolve(safeName);

            audio.transferTo(dest.toFile());

            int duracionSeg;
            try {
                duracionSeg = AudioUtils.getAudioDuracionSegundos(dest);
            } catch (Exception e) {
                duracionSeg = 0; // no reventar si ffprobe falla
            }

            String urlAudio = "/media/audio/" + safeName;

            Cancion cancion = new Cancion();
            cancion.setTitulo(dto.titulo());
            cancion.setDescripcion(dto.descripcion());
            cancion.setPortadaUrl(dto.portadaUrl());
            cancion.setUrlAudio(urlAudio);
            cancion.setDuracionSegundos(duracionSeg);
            cancion.setArtista(artista);
            cancion.setAlbum(album);

            Cancion guardada = cancionRepository.save(cancion);
            return cancionMapper.toDto(guardada);

        } catch (IOException e) {
            getLogger(CancionService.class)
                    .error("Fallo I/O guardando audio en {}: {}", audioDir, e.toString(), e);

            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR, "Error procesando el audio"
            );
        }
    }


    // Editar
    @Transactional
    public CancionResponseDTO editarCancion(Long id, CancionRequestDTO dto) {

        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new CancionNotFoundException(id));

        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        Album album = null;
        if (dto.albumId() != null) {
            album = albumRepository.findById(dto.albumId())
                    .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
        }

        // Actualizar campos básicos
        cancion.setTitulo(dto.titulo());
        cancion.setDescripcion(dto.descripcion());
        cancion.setPortadaUrl(dto.portadaUrl());
        cancion.setArtista(artista);
        cancion.setAlbum(album);


        if (dto.urlAudio() != null && !dto.urlAudio().isBlank()) {
            cancion.setUrlAudio(dto.urlAudio());
        }

        Cancion actualizada = cancionRepository.save(cancion);
        return cancionMapper.toDto(actualizada);
    }

    @Transactional
    public void eliminarCancion(Long id) {
        if (!cancionRepository.existsById(id)) {
            throw new CancionNotFoundException(id);
        }
        cancionRepository.deleteById(id);
    }

    //  LISTADOS

    //  Listar paginado
    public Page<CancionResponseDTO> listarCanciones(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cancionRepository.findAll(pageable)
                .map(cancionMapper::toDto);
    }


    public Page<CancionResponseDTO> listarCancionesPorAlbum(Long albumId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cancionRepository.findByAlbumId(albumId, pageable)
                .map(cancionMapper::toDto);
    }

    public Page<CancionResponseDTO> listarSinglesPorArtista(Long artistaId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cancionRepository.findByArtistaIdAndAlbumIsNull(artistaId, pageable)
                .map(cancionMapper::toDto);
    }


    // Ver detalle
    public CancionResponseDTO verDetalleCancion(Long id) {
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new CancionNotFoundException(id));
        return cancionMapper.toDto(cancion);
    }

    // Listar por artista
    public Page<CancionResponseDTO> listarCancionesPorArtista(Long artistaId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cancionRepository.findByArtistaId(artistaId, pageable)
                .map(cancionMapper::toDto);
    }

    // Buscar (paginado)
    public Page<CancionResponseDTO> buscarCanciones(String q, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cancionRepository.findByTituloContainingIgnoreCase(q, pageable)
                .map(cancionMapper::toDto);
    }



    // Mantener la extension de archivo
    private static String buildSafeFileName(String original) {
        if (original == null || original.isBlank()) original = "audio";

        // 1) separar base y extensión
        int dot = original.lastIndexOf('.');
        String base = (dot > 0) ? original.substring(0, dot) : original;
        String ext  = (dot > 0 && dot < original.length() - 1) ? original.substring(dot + 1).toLowerCase() : "";

        // 2) sanear base (solo [a-zA-Z0-9-_.], resto -> "_")
        base = base.replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                .replaceAll("_+", "_")
                .replaceAll("\\.+", ".")
                .replaceAll("^[_\\.]+|[_\\.]+$", "");
        if (base.isBlank()) base = "audio";

        // 3) limitar longitud razonable
        if (base.length() > 60) base = base.substring(0, 60);

        // 4) UUID + base + (ext opcional)
        String uuid = java.util.UUID.randomUUID().toString();
        return ext.isEmpty() ? uuid + "_" + base : uuid + "_" + base + "." + ext;
    }


}

