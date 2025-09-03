package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin - Cancion", description = "Endpoints de administración para gestionar canciones")
public interface CancionAdminApi {

    @Operation(
            summary = "Crear cancion",
            description = "Crea una nueva canción.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CancionRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Crear cancion",
                                    value = """
                                    {
                                      "titulo": "Mon Amour",
                                      "descripcion": "Versión original",
                                      "duracion": 184,
                                      "portadaUrl": "https://cdn.example.com/monamour.jpg",
                                      "urlAudio": "https://cdn.example.com/monamour.mp3",
                                      "artistaId": 1
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Cancion creada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CancionResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content)
    ResponseEntity<CancionResponseDTO> crearCancion(@RequestBody CancionRequestDTO dto);

    @Operation(
            summary = "Editar cancion",
            description = "Actualiza una canción existente por ID.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CancionRequestDTO.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "Cancion actualizada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CancionResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cancion/Artista no encontrado", content = @Content)
    ResponseEntity<CancionResponseDTO> editarCancion(Long id, @RequestBody CancionRequestDTO dto);

    @Operation(summary = "Eliminar cancion", description = "Elimina una canción por ID.")
    @ApiResponse(responseCode = "204", description = "Eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Cancion no encontrada", content = @Content)
    ResponseEntity<Void> eliminarCancion(Long id);
}
