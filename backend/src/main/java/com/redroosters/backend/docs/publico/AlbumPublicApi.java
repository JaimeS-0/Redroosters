package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.AlbumResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

@Tag(name = "Public - Álbum", description = "Endpoints públicos para consultar álbumes")
public interface AlbumPublicApi {

    @Operation(
            summary = "Listar álbumes (paginado)",
            description = "Devuelve la lista de álbumes con paginación y ordenación."
    )
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AlbumResponseDTO.class))))
    ResponseEntity<Page<AlbumResponseDTO>> listarAlbumes(
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'titulo')") String sort
    );

    @Operation(
            summary = "Detalle de un álbum",
            description = "Devuelve el detalle de un álbum por su ID."
    )
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AlbumResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Álbum no encontrado", content = @Content)
    ResponseEntity<AlbumResponseDTO> verDetalle(Long id);
}
