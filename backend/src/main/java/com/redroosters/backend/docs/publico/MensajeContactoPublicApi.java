package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.MensajeContactoRequestDTO;
import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Public - Contacto", description = "Enviar mensajes de contacto")
public interface MensajeContactoPublicApi {

    @Operation(summary = "Crear mensaje de contacto",
            description = "Crea un mensaje de contacto público.")
    @RequestBody(required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensajeContactoRequestDTO.class),
                    examples = @ExampleObject(name="Ejemplo",
                            value = """
                {
                  "nombre": "Federico",
                  "email": "Federico@example.com",
                  "asunto": "Colaboración",
                  "mensaje": "Hola, me interesa subir un tema nuevo."
                }
                """
                    )))
    @ApiResponse(responseCode = "200", description = "Mensaje creado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensajeContactoResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    ResponseEntity<MensajeContactoResponseDTO> crearMensaje(MensajeContactoRequestDTO dto);
}
