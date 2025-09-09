package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Admin - Cancion", description = "Endpoints de administración para gestionar canciones")
public interface CancionAdminApi {

    @Operation(
            summary = "Crear canción (multipart)",
            description = "Crea una canción subiendo el audio y los metadatos en la parte 'datos' (JSON)."
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(description = "Partes: 'datos' (JSON) y 'audio' (file)"),
                    examples = @ExampleObject(
                            name = "form-data (mostrar JSON de 'datos')",
                            value = """
                ----- form-data -----
                Key: datos (Text)
                Value:
                {
                  "titulo": "Mi tema",
                  "descripcion": "prueba",
                  "portadaUrl": "https://picsum.photos/400/400",
                  "artistaId": 1,
                  "albumId": 2
                }
                
                Key: audio (File)
                Value: <selecciona tu .mp3>
                """
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Canción creada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CancionResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Artista/Álbum no encontrado", content = @Content)
    ResponseEntity<CancionResponseDTO> crearConArchivo(String datosJson, MultipartFile audio);

    @Operation(
            summary = "Editar canción (JSON)",
            description = "Actualiza metadatos de una canción existente por ID (sin re-subir el audio).",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CancionRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Editar canción",
                                    value = """
                    {
                      "titulo": "Mi tema (editado)",
                      "descripcion": "ahora mejor",
                      "portadaUrl": "https://picsum.photos/500/500",
                      "artistaId": 1,
                      "albumId": 2
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Canción actualizada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CancionResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Canción/Artista/Álbum no encontrado", content = @Content)
    ResponseEntity<CancionResponseDTO> editarCancion(
            @Parameter(description = "ID de la canción", required = true) Long id,
            @RequestBody CancionRequestDTO dto
    );

    @Operation(summary = "Eliminar canción", description = "Elimina una canción por ID.")
    @ApiResponse(responseCode = "204", description = "Eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Canción no encontrada", content = @Content)
    ResponseEntity<Void> eliminarCancion(
            @Parameter(description = "ID de la canción", required = true) Long id
    );
}
