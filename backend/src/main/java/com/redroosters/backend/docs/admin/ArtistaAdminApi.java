package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin - Artista", description = "Endpoints de administración para gestionar artistas")
public interface ArtistaAdminApi {

    @Operation(
            summary = "Crear artista",
            description = "Crea un nuevo artista.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Crear artista",
                                    value = """
                                    {
                                      "nombre": "Aitana",
                                      "descripcion": "Pop español",
                                      "portadaUrl": "https://cdn.example.com/aitana.jpg",
                                      "destacado": true
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Artista creado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArtistaResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    ResponseEntity<ArtistaResponseDTO> crearArtista(@RequestBody ArtistaRequestDTO dto);

    @Operation(
            summary = "Editar artista",
            description = "Actualiza un artista existente por ID.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "Artista actualizado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArtistaResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content)
    ResponseEntity<ArtistaResponseDTO> editarArtista(Long id, @RequestBody ArtistaRequestDTO dto);

    @Operation(summary = "Eliminar artista", description = "Elimina un artista por ID.")
    @ApiResponse(responseCode = "204", description = "Eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content)
    ResponseEntity<Void> eliminarArtista(Long id);
}
