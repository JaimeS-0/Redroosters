package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.CancionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Public - Canción", description = "Endpoints públicos para consultar canciones")
public interface CancionPublicApi {

    @Operation(summary = "Listar canciones (paginado)",
            description = "Devuelve la lista de canciones con paginación y ordenación.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CancionResponseDTO.class))))
    ResponseEntity<Page<CancionResponseDTO>> listarCanciones(
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'titulo')") String sort
    );

    @Operation(summary = "Canciones de un álbum (paginado)",
            description = "Devuelve las canciones pertenecientes a un álbum.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CancionResponseDTO.class))))
    ResponseEntity<Page<CancionResponseDTO>> listarCancionesPorAlbum(
            @Parameter(description = "ID del álbum") Long albumId,
            int page, int size, String sort
    );

    @Operation(summary = "Singles de un artista (paginado)",
            description = "Canciones de un artista que no pertenecen a ningún álbum.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CancionResponseDTO.class))))
    ResponseEntity<Page<CancionResponseDTO>> listarSinglesPorArtista(
            @Parameter(description = "ID del artista") Long artistaId,
            int page, int size, String sort
    );

    @Operation(summary = "Detalle de canción", description = "Devuelve el detalle de una canción por ID.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CancionResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<CancionResponseDTO> verDetalle(
            @Parameter(description = "ID de la canción") Long id
    );

}
