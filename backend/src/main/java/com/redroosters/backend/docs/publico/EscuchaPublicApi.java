package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.TopCancionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Public - Escuchas", description = "Estadísticas públicas de escuchas")
public interface EscuchaPublicApi {

    @Operation(summary = "Contador global de escuchas de una canción",
            description = "Devuelve el total de escuchas globales para una canción por ID.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<Long> contadorGlobal(
            @Parameter(description = "ID de la canción") Long id
    );

    @Operation(summary = "Top global de canciones más escuchadas",
            description = "Devuelve el ranking global (límite parametrizable).")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TopCancionDTO.class))))
    ResponseEntity<List<TopCancionDTO>> top(
            @Parameter(description = "Número de resultados (por defecto 10)") int limit
    );
}
