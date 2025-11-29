package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Admin - Canciones",
        description = "Endpoints privados para crear, editar y eliminar canciones"
)
public interface CancionAdminApi {

    @Operation(
            summary = "Crear canción con archivo",
            description = """
                    Crea una nueva canción subiendo el archivo de audio (.mp3).
                    Se envía como multipart/form-data con estas partes:
                    - 'datos': JSON con CancionRequestDTO
                    - 'audio': archivo de audio MP3 (obligatorio)
                    - 'portada': imagen de portada opcional
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Canción creada correctamente",
                    content = @Content(schema = @Schema(implementation = CancionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o archivo no válido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    ResponseEntity<CancionResponseDTO> crearConArchivo(
            @Parameter(
                    description = "JSON con los datos de la canción (CancionRequestDTO) serializado como String",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CancionRequestDTO.class)
                    )
            )
            @RequestPart("datos") String datosJson,

            @Parameter(
                    description = "Archivo de audio en formato MP3. Debe ir en la parte 'audio'.",
                    required = true,
                    content = @Content(mediaType = "audio/mpeg")
            )
            @RequestPart("audio") MultipartFile audio,

            @Parameter(
                    description = "Imagen de portada de la canción (opcional).",
                    content = @Content(mediaType = "image/*")
            )
            @RequestPart(value = "portada", required = false) MultipartFile portada
    );

    @Operation(
            summary = "Editar canción",
            description = """
                    Edita una canción existente.
                    Se envía como multipart/form-data:
                    - 'datos': JSON con CancionRequestDTO (campos a actualizar)
                    - 'audio': nuevo archivo de audio MP3 opcional
                    - 'portada': nueva portada opcional
                    Si no se envía audio/portada, se mantienen los actuales.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Canción editada correctamente",
                    content = @Content(schema = @Schema(implementation = CancionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o JSON de 'datos' no válido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Canción no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    ResponseEntity<CancionResponseDTO> editarCancion(
            @Parameter(
                    description = "ID de la canción a editar",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "JSON con los datos de la canción (CancionRequestDTO) serializado como String",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CancionRequestDTO.class)
                    )
            )
            @RequestPart("datos") String datosJson,

            @Parameter(
                    description = "Nuevo archivo de audio MP3 (opcional)",
                    content = @Content(mediaType = "audio/mpeg")
            )
            @RequestPart(name = "audio", required = false) MultipartFile audio,

            @Parameter(
                    description = "Nueva imagen de portada (opcional)",
                    content = @Content(mediaType = "image/*")
            )
            @RequestPart(name = "portada", required = false) MultipartFile portada
    );

    @Operation(
            summary = "Eliminar canción",
            description = "Elimina una canción existente por su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Canción eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Canción no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    ResponseEntity<Void> eliminarCancion(
            @Parameter(
                    description = "ID de la canción a eliminar",
                    example = "1"
            )
            @PathVariable Long id
    );
}
