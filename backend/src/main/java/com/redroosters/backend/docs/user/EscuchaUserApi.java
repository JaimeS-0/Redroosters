package com.redroosters.backend.docs.user;

import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User - Escuchas", description = "Acciones del usuario autenticado relacionadas con escuchas")
public interface EscuchaUserApi {

    @Operation(
            summary = "Registrar escucha",
            description = "Registra una escucha para la canción indicada. Requiere JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Escucha registrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EscuchaResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<EscuchaResponseDTO> registrarEscucha(
            @Parameter(hidden = true) Usuario usuario,
            @Parameter(description = "ID de la canción", required = true) Long idCancion
    );
}
