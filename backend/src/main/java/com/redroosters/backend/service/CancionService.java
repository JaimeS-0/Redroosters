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
    private final AlmacenamientoService almacenamientoService;


    @Value("${app.uploads.audio-dir}")
    private String audioDir;

    public CancionService(CancionRepository cancionRepository,
                          ArtistaRepository artistaRepository,
                          AlbumRepository albumRepository,
                          CancionMapper cancionMapper,
                          AlmacenamientoService almacenamientoService) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.albumRepository = albumRepository;
        this.cancionMapper = cancionMapper;
        this.almacenamientoService = almacenamientoService;

    }


     // Crear cancion SUBIENDO archivo. Guarda el fichero en disco, calcula duración con ffprobe,

    @Transactional
    public CancionResponseDTO crearConArchivo(CancionRequestDTO dto,
                                              MultipartFile audio,
                                              MultipartFile portadaFile) {

        // Validaciones de dominio
        Artista artista = artistaRepository.findById(dto.artistaId())
                .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));

        Album album = null;
        if (dto.albumId() != null) {
            album = albumRepository.findById(dto.albumId())
                    .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
        }

        // ---- Guardar portada si viene ----
        String portadaUrl = null;
        if (portadaFile != null && !portadaFile.isEmpty()) {
            portadaUrl = almacenamientoService.guardarPortada(portadaFile);
        }

        // Validaciones de request
        if (audio == null || audio.isEmpty()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Debes adjuntar el archivo de audio en la parte 'audio'"
            );
        }
        if (audioDir == null || audioDir.isBlank()) {
            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR,
                    "app.uploads.audio-dir no está configurado"
            );
        }

        String rutaOriginal = audio.getOriginalFilename();
        if (rutaOriginal == null || !rutaOriginal.toLowerCase().endsWith(".mp3")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El archivo debe tener extensión .mp3"
            );
        }

        try {
            Path folder = Paths.get(audioDir).toAbsolutePath().normalize();
            Files.createDirectories(folder);

            String original = (audio.getOriginalFilename() != null)
                    ? audio.getOriginalFilename()
                    : "audio.mp3";

            String safeName = buildSafeFileName(original);
            Path dest = folder.resolve(safeName);
            audio.transferTo(dest.toFile());

            int duracionSeg;
            try {
                duracionSeg = AudioUtils.getAudioDuracionSegundos(dest);
            } catch (Exception e) {
                duracionSeg = 0;
            }

            String urlAudio = "/media/audio/" + safeName;

            Cancion cancion = new Cancion();
            cancion.setTitulo(dto.titulo());
            cancion.setDescripcion(dto.descripcion());

            // 🔴 IMPORTANTE: usar la URL calculada, y si fuera null,
            // caer a lo que venga en el DTO (por si algun caso especial):
            cancion.setPortada(
                    (portadaUrl != null && !portadaUrl.isBlank())
                            ? portadaUrl
                            : dto.portada()
            );

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


    @Transactional
    public CancionResponseDTO editarCancion(Long id,
                                            CancionRequestDTO dto,
                                            MultipartFile audio,
                                            MultipartFile portadaFile) {

        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new CancionNotFoundException(id));

        // 🔹 TÍTULO: solo si viene
        if (dto.titulo() != null) {
            cancion.setTitulo(dto.titulo());
        }

        // 🔹 DESCRIPCION: solo si viene
        if (dto.descripcion() != null) {
            cancion.setDescripcion(dto.descripcion());
        }

        // 🔹 ARTISTA: solo si viene artistaId
        if (dto.artistaId() != null) {
            Artista artista = artistaRepository.findById(dto.artistaId())
                    .orElseThrow(() -> new ArtistaNotFoundException(dto.artistaId()));
            cancion.setArtista(artista);
        }

        // 🔹 ALBUM: solo si viene albumId
        if (dto.albumId() != null) {
            Album album = albumRepository.findById(dto.albumId())
                    .orElseThrow(() -> new AlbumNotFoundException(dto.albumId()));
            cancion.setAlbum(album);
        }

        // 🔹 PORTADA nueva si viene archivo
        if (portadaFile != null && !portadaFile.isEmpty()) {
            String portadaUrl = almacenamientoService.guardarPortada(portadaFile);
            cancion.setPortada(portadaUrl);
        }

        // 🔹 AUDIO nuevo si viene archivo
        if (audio != null && !audio.isEmpty()) {
            if (audioDir == null || audioDir.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "app.uploads.audio-dir no esta configurado"
                );
            }

            String rutaOriginal = audio.getOriginalFilename();
            if (rutaOriginal == null || !rutaOriginal.toLowerCase().endsWith(".mp3")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El archivo debe tener extension .mp3"
                );
            }

            try {
                Path folder = Paths.get(audioDir).toAbsolutePath().normalize();
                Files.createDirectories(folder);

                String original = (audio.getOriginalFilename() != null)
                        ? audio.getOriginalFilename()
                        : "audio.mp3";

                String safeName = buildSafeFileName(original);
                Path dest = folder.resolve(safeName);
                audio.transferTo(dest.toFile());

                int duracionSeg;
                try {
                    duracionSeg = AudioUtils.getAudioDuracionSegundos(dest);
                } catch (Exception e) {
                    duracionSeg = 0;
                }

                String urlAudio = "/media/audio/" + safeName;
                cancion.setUrlAudio(urlAudio);
                cancion.setDuracionSegundos(duracionSeg);

            } catch (IOException e) {
                getLogger(CancionService.class)
                        .error("Fallo I/O guardando audio en {}: {}", audioDir, e.toString(), e);

                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error procesando el audio"
                );
            }
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

