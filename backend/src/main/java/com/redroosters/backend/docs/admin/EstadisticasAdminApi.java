package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.EstadisticasDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin - Estadísticas", description = "Endpoints de administración para ver estadísticas globales")
public interface EstadisticasAdminApi {

    @Operation(
            summary = "Obtener estadísticas globales",
            description = "Devuelve las estadísticas globales de la aplicación: " +
                    "totales de canciones, artistas, álbumes, escuchas, " +
                    "así como la canción más escuchada y la canción con más likes."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas correctamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstadisticasDTO.class)
            )
    )
    ResponseEntity<EstadisticasDTO> getStats();
}
