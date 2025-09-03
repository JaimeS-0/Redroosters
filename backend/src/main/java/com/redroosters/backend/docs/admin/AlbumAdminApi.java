package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin - Album", description = "Endpoints de administración para gestionar albumes")
public interface AlbumAdminApi {

    @Operation(
            summary = "Crear album",
            description = "Crea un nuevo album asociado a un artista.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlbumRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Crear album",
                                    value = """
                                    {
                                      "titulo": "Midnights",
                                      "descripcion": "Nuevo lanzamiento",
                                      "portadaUrl": "https://cdn.example.com/midnights.jpg",
                                      "artistaId": 1,
                                      "cancionesIds": [10, 11, 12]
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Album creado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AlbumResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Artista/Cancion no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflicto de datos", content = @Content)
    ResponseEntity<AlbumResponseDTO> crearAlbum(@RequestBody AlbumRequestDTO dto);

    @Operation(
            summary = "Editar album",
            description = "Edita los campos de un album existente por ID.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlbumRequestDTO.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "Album actualizado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AlbumResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Album no encontrado", content = @Content)
    ResponseEntity<AlbumResponseDTO> editarAlbum(Long id, @RequestBody AlbumRequestDTO dto);

    @Operation(summary = "Eliminar album", description = "Elimina un album por ID.")
    @ApiResponse(responseCode = "204", description = "Eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Album no encontrado", content = @Content)
    ResponseEntity<Void> eliminarAlbum(Long id);
}
