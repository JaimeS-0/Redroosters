package com.redroosters.backend.docs.publico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Public - Likes", description = "Conteos públicos de likes")
public interface LikePublicApi {

    @Operation(summary = "Contador de likes por canción",
            description = "Devuelve el total de likes que tiene una canción.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<Long> count(
            @Parameter(description = "ID de la canción") Long cancionId
    );
}
