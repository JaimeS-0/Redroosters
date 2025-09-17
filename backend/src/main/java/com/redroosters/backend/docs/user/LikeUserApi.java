package com.redroosters.backend.docs.user;

import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.dto.LikedResponse;
import com.redroosters.backend.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "User - Likes", description = "Gestión de likes del usuario autenticado")
public interface LikeUserApi {

    @Operation(
            summary = "Dar like a una canción",
            description = "Crea el like del usuario para la canción indicada. Requiere JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Like creado")
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<Void> darLike(
            @Parameter(description = "ID de la canción", required = true) Long cancionId,
            @Parameter(description = "Nombre (opcional)", examples = @ExampleObject(value = "Taylor Swift")) String nombre,
            @Parameter(description = "Título (opcional)", examples = @ExampleObject(value = "...Ready For It?")) String titulo,
            @Parameter(hidden = true) Usuario usuario
    );

    @Operation(
            summary = "Quitar like de una canción",
            description = "Elimina el like del usuario para la canción indicada (idempotente). Requiere JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Like eliminado")
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<Void> quitarLike(
            @Parameter(description = "ID de la canción", required = true) Long cancionId,
            @Parameter(description = "Título (opcional)") String titulo,
            @Parameter(hidden = true) Usuario usuario
    );

    @Operation(
            summary = "¿Existe like?",
            description = "Devuelve si el usuario actual ha dado like a la canción. Requiere JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LikedResponse.class)))
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    ResponseEntity<LikedResponse> existeLike(
            @Parameter(description = "ID de la canción", required = true) Long cancionId,
            @Parameter(hidden = true) Usuario usuario
    );

    @Operation(
            summary = "Listar favoritos (paginado)",
            description = "Canciones marcadas como favoritas por el usuario actual. Requiere JWT.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CancionResponseDTO.class))))
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    ResponseEntity<Page<CancionResponseDTO>> listarFavoritos(
            @Parameter(description = "Página (0-index)") int page,
            @Parameter(description = "Tamaño de página") int size,
            @Parameter(description = "Campo de ordenación (p. ej. 'titulo')") String sort,
            @Parameter(hidden = true) Usuario usuario
    );
}
