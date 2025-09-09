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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    // CRUD
/*
    @Transactional
    public CancionResponseDTO crearCancion(CancionRequestDTO dto) {

        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        Album album = null;
        if (dto.albumId() != null) {
            album = albumRepository.findById(dto.albumId())
                    .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
        }

        // Mapear DTO → Entidad
        Cancion cancion = cancionMapper.toEntity(dto);

        cancion.setArtista(artista);
        cancion.setAlbum(album);

        Cancion guardada = cancionRepository.save(cancion);
        return cancionMapper.toDto(guardada);
    }
    */


     // Crear cancion SUBIENDO archivo. Guarda el fichero en disco, calcula duración con ffprobe,

    @Transactional
    public CancionResponseDTO crearConArchivo(CancionRequestDTO dto, MultipartFile audio) {
        try {
            Artista artista = artistaRepository.findById(dto.artistaId())
                    .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

            Album album = null;
            if (dto.albumId() != null) {
                album = albumRepository.findById(dto.albumId())
                        .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
            }

            // Guardar el fichero
            Path folder = Paths.get(audioDir).toAbsolutePath().normalize();
            Files.createDirectories(folder);

            String original = (audio.getOriginalFilename() != null) ? audio.getOriginalFilename() : "audio.mp3";
            String safeName = buildSafeFileName(original);
            Path dest = folder.resolve(safeName);
            audio.transferTo(dest.toFile());

            // Calcular duración (segundos) con ffprobe
            int duracionSeg = AudioUtils.getAudioDuracionSegundos(dest);

            // URL pública servida por tu app/NGINX
            String urlAudio = "/media/audio/" + safeName;

            // Guardar entidad
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

        } catch (Exception e) {
            throw new RuntimeException("Error procesando el audio", e);
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

    // Publico USER
    public List<CancionResponseDTO> listarCanciones() {
        List<Cancion> canciones = cancionRepository.findAll();
        return cancionMapper.toDtoList(canciones);
    }

    // Publico USER: canciones por artista (tengan o no álbum)
    public List<CancionResponseDTO> listarPorArtista(Long artistaId) {
        List<Cancion> canciones = cancionRepository.findByArtistaId(artistaId);
        return cancionMapper.toDtoList(canciones);
    }

    // Publico USER: canciones por álbum
    public List<CancionResponseDTO> listarPorAlbum(Long albumId) {
        List<Cancion> canciones = cancionRepository.findByAlbumId(albumId);
        return cancionMapper.toDtoList(canciones);
    }

    // Privado ADMIN: canciones del artista sin álbum asignado (para construir álbumes)
    public List<CancionResponseDTO> listarCancionesSinAlbum(Long artistaId) {
        List<Cancion> canciones = cancionRepository.findByArtistaIdAndAlbumIsNull(artistaId);
        return cancionMapper.toDtoList(canciones);
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

