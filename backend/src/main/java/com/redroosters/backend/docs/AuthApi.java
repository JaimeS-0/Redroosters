package com.redroosters.backend.docs;

import com.redroosters.backend.dto.LoginRequestDTO;
import com.redroosters.backend.dto.LoginResponseDTO;
import com.redroosters.backend.dto.RegistroRequestDTO;
import com.redroosters.backend.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Endpoints de autenticacion")
public interface AuthApi {

    @Operation(
            summary = "Inicia sesion",
            description = "Valida credenciales y devuelve un token JWT junto con datos basicos del usuario.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Login correcto",
                                            value = """
                                {
                                  "email": "paco@example.com",
                                  "password": "12345678"
                                }
                                """
                                    )
                            }
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Login correcto",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales invalidas", content = @Content)
    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request);

    @Operation(
            summary = "Registro de usuario",
            description = "Crea un usuario nuevo."
            ,
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistroRequestDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Registro basico",
                                            value = """
                                {
                                  "username": "paco_perez",
                                  "email": "paco@example.com",
                                  "password": "12345678"
                                }
                                """
                                    )
                            }
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Registro correcto",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email ya registrado", content = @Content)
    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
    ResponseEntity<UsuarioResponseDTO> register(@RequestBody RegistroRequestDTO request);
}
