package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
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
        name = "Admin - Artistas",
        description = "Endpoints privados para crear, editar y eliminar artistas"
)
public interface ArtistaAdminApi {

    @Operation(
            summary = "Crear artista",
            description = """
                    Crea un nuevo artista.
                    Se envía como multipart/form-data con las partes:
                    - 'nombre': nombre del artista (texto)
                    - 'descripcion': descripción opcional
                    - 'destacado': true/false
                    - 'portada': imagen de portada opcional
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Artista creado correctamente",
                    content = @Content(schema = @Schema(implementation = ArtistaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
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
    ResponseEntity<ArtistaResponseDTO> crearArtista(
            @Parameter(
                    description = "Nombre del artista",
                    example = "Taylor Swift",
                    required = true
            )
            @RequestPart("nombre") String nombre,

            @Parameter(
                    description = "Descripción del artista",
                    example = "Cantante y compositora de pop"
            )
            @RequestPart(value = "descripcion", required = false) String descripcion,

            @Parameter(
                    description = "Indica si el artista es destacado (true/false)",
                    example = "true"
            )
            @RequestPart(value = "destacado", required = false) String destacadoStr,

            @Parameter(
                    description = "Imagen de portada del artista",
                    content = @Content(mediaType = "image/*")
            )
            @RequestPart(value = "portada", required = false) MultipartFile portada
    );

    @Operation(
            summary = "Editar artista",
            description = """
                    Edita un artista existente.
                    Todas las partes son opcionales; solo se actualizarán los campos enviados.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Artista actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ArtistaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artista no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    ResponseEntity<ArtistaResponseDTO> editarArtista(
            @Parameter(
                    description = "ID del artista a editar",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Nuevo nombre del artista",
                    example = "Taylor Swift"
            )
            @RequestPart(value = "nombre", required = false) String nombre,

            @Parameter(
                    description = "Nueva descripción del artista"
            )
            @RequestPart(value = "descripcion", required = false) String descripcion,

            @Parameter(
                    description = "Indica si el artista es destacado (true/false)"
            )
            @RequestPart(value = "destacado", required = false) String destacadoStr,

            @Parameter(
                    description = "Nueva imagen de portada del artista",
                    content = @Content(mediaType = "image/*")
            )
            @RequestPart(value = "portada", required = false) MultipartFile portada
    );

    @Operation(
            summary = "Eliminar artista",
            description = "Elimina un artista existente por su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Artista eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artista no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    ResponseEntity<Void> eliminarArtista(
            @Parameter(
                    description = "ID del artista a eliminar",
                    example = "1"
            )
            @PathVariable Long id
    );
}
