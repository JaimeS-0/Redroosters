package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.BusquedaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Public - Buscador", description = "Endpoint público para buscar artistas, canciones y álbumes")
public interface BuscarPublicApi {

    @Operation(
            summary = "Buscar (artistas, canciones y álbumes)",
            description = """
                Busca simultáneamente en artistas, canciones y álbumes.
                Los resultados se devuelven unificados y paginados, ordenados por relevancia y título.
                """)
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BusquedaDTO.class)),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo-OK",
                                    value = """
                                            [
                                              { "id": 1, "tipo": "CANCION", "titulo": "Lentamente", "subtitulo": "Ana Mena" },
                                              { "id": 2, "tipo": "ALBUM",   "titulo": "Bello Drama", "subtitulo": "Ana Mena" },
                                              { "id": 3, "tipo": "ARTISTA", "titulo": "Ana Mena",    "subtitulo": null }
                                            ]
                                            """
                            )
                    }
            )
    )
    @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos", content = @Content)
    ResponseEntity<List<BusquedaDTO>> buscar(
            @Parameter(description = "Texto de búsqueda", required = true, example = "Taylor")
            String q,
            @Parameter(description = "Página (0-index). Ej: 0, 1, 2...", example = "0")
            int page,
            @Parameter(description = "Tamaño de página (nº de resultados por página). Mínimo 1", example = "10")
            int size
    );
}
