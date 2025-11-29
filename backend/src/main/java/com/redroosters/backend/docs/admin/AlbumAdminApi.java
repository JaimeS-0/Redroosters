package com.redroosters.backend.docs.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Admin - Albumes",
        description = "Endpoints privados para crear, editar y eliminar albumes"
)
public interface AlbumAdminApi {

    @Operation(
            summary = "Crear album",
            description = """
                    Crea un nuevo album para un artista concreto.
                    El cuerpo se envía como multipart/form-data:
                    - parte 'datos': JSON con AlbumRequestDTO (serializado a String)
                    - parte 'portada': fichero de imagen opcional
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Album creado correctamente",
                    content = @Content(schema = @Schema(implementation = AlbumResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o JSON mal formado",
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
    @PostMapping(
            value = "/album",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<AlbumResponseDTO> crearAlbum(
            @Parameter(
                    description = "JSON con los datos del album (AlbumRequestDTO) serializado como String",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumRequestDTO.class)
                    )
            )
            @RequestPart("datos") String datosJson,

            @Parameter(
                    description = "Imagen de portada del album (opcional)",
                    content = @Content(mediaType = "image/*")
            )
            @RequestPart(value = "portada", required = false) MultipartFile portada
    ) throws JsonProcessingException;


    @Operation(
            summary = "Editar album",
            description = "Edita un album existente identificado por su id. El cuerpo se envía como JSON (AlbumRequestDTO)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Album editado correctamente",
                    content = @Content(schema = @Schema(implementation = AlbumResponseDTO.class))
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
                    description = "Album no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PutMapping(
            value = "/album/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<AlbumResponseDTO> editarAlbum(
            @Parameter(
                    description = "ID del album a editar",
                    example = "1"
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del album",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumRequestDTO.class)
                    )
            )
            @RequestBody AlbumRequestDTO dto
    );


    @Operation(
            summary = "Eliminar album",
            description = "Elimina un album existente por su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Album eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Album no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @DeleteMapping("/album/{id}")
    ResponseEntity<Void> eliminarAlbum(
            @Parameter(
                    description = "ID del album a eliminar",
                    example = "1"
            )
            @PathVariable Long id
    );
}
