package com.redroosters.backend.docs.admin;

import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Admin - Mensajes de Contacto", description = "Endpoints de administración para revisar mensajes enviados por los usuarios")
public interface MensajeContactoAdminApi {

    @Operation(summary = "Listar mensajes", description = "Devuelve la lista de mensajes recibidos.")
    @ApiResponse(responseCode = "200", description = "Listado de mensajes",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensajeContactoResponseDTO.class)))
    ResponseEntity<List<MensajeContactoResponseDTO>> listarMensajes();

    @Operation(summary = "Obtener mensaje por ID", description = "Devuelve el detalle de un mensaje específico.")
    @ApiResponse(responseCode = "200", description = "Mensaje encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensajeContactoResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Mensaje no encontrado", content = @Content)
    ResponseEntity<MensajeContactoResponseDTO> obtenerMensaje(Long id);
}
