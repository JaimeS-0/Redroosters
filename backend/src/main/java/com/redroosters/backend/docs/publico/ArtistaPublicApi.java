package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.dto.ArtistaDetalleDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Public - Artista", description = "Endpoints públicos para consultar artistas")
public interface ArtistaPublicApi {

    @Operation(summary = "Listar artistas (paginado)",
            description = "Devuelve artistas paginados. En tu front usas más el random de 6.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ArtistaResponseDTO.class))))
    ResponseEntity<Page<ArtistaResponseDTO>> listarTodos(
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'nombre')") String sort
    );

    @Operation(summary = "Listar 6 artistas aleatorios",
            description = "Devuelve N artistas aleatorios. Puedes excluir IDs ya mostrados.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ArtistaResponseDTO.class))))
    ResponseEntity<List<ArtistaResponseDTO>> artistasRandom(
            @Parameter(description = "Número de artistas a devolver (por defecto 6)")
            int size,
            @Parameter(description = "IDs a excluir, separados por comas. Ej: 1,2,3",
                    examples = @ExampleObject(value = "1,5,9"))
            List<Long> excludeIds
    );

    @Operation(summary = "Detalle de un artista", description = "Devuelve datos básicos del artista.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArtistaResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content)
    ResponseEntity<ArtistaDetalleDTO> verDetalle(
            @Parameter(description = "ID del artista") Long id
    );

    @Operation(summary = "Álbumes de un artista (paginado)",
            description = "Devuelve los álbumes de un artista por su ID, con paginación.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AlbumResponseDTO.class))))
    ResponseEntity<Page<AlbumResponseDTO>> albumesDeArtista(
            @Parameter(description = "ID del artista") Long id,
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'titulo')") String sort
    );

    @Operation(summary = "Canciones de un artista (paginado)",
            description = "Devuelve las canciones de un artista por su ID, con paginación.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CancionResponseDTO.class))))
    ResponseEntity<Page<CancionResponseDTO>> cancionesDeArtista(
            @Parameter(description = "ID del artista") Long id,
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'titulo')") String sort
    );
}
