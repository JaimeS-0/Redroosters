package com.redroosters.backend.docs.publico;

import com.redroosters.backend.dto.BusquedaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "Buscador publico",
        description = "Permite buscar artistas, albumes y canciones desde la web pública."
)
public interface BuscarPublicApi {

    @Operation(
            summary = "Buscar contenido",
            description = """
                Devuelve una lista combinada de resultados que coinciden con el texto buscado.
                Incluye:
                - Artistas  
                - Álbumes  
                - Canciones

                Se usa en el buscador de la web.
            """,
            parameters = {
                    @Parameter(
                            name = "q",
                            description = "Texto que introduce el usuario para buscar.",
                            required = true,
                            example = "Aitana"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de resultados encontrados."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parámetros inválidos."
                    )
            }
    )
    @GetMapping("/buscar")
    List<BusquedaDTO> buscar(
            @RequestParam("q") String q
    );
}
