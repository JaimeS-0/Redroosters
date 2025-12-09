package com.redroosters.backend.controller;

import com.redroosters.backend.controller.publico.BuscarPublicController;
import com.redroosters.backend.dto.BusquedaDTO;
import com.redroosters.backend.service.BuscarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Este test comprueba el funcionamiento del endpoint público de búsqueda
// (/api/public/buscar) sin necesidad de levantar el servidor ni usar JWT.
// Se usa @WebMvcTest para probar solo el controlador de búsqueda,
// simulando peticiones HTTP con MockMvc.
 @WebMvcTest(
        controllers = BuscarPublicController.class,
        excludeFilters = {
                // Excluimos la configuración real de seguridad (JWT) para evitar errores
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.JwtAuthenticationFilter.class)
        }
)
@Import(BuscarPublicControllerTest.TestSecurityConfig.class) // Añadimos seguridad mínima solo para el test
class BuscarPublicControllerTest {

    // MockMvc permite simular peticiones HTTP sin levantar el servidor real
    @Autowired MockMvc mvc;

    // Creamos un mock del servicio que usa el controlador (BuscarService)
    // Así evitamos acceder a la base de datos o lógica interna
    @MockitoBean
    BuscarService buscarService;


    // Configuración de seguridad mínima usada solo durante este test.
    // Permite acceso libre a /api/public/** y evita errores 401/403
    // al no cargar la seguridad JWT real.
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurity(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // desactiva CSRF para peticiones POST/GET en test
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/public/**").permitAll() // rutas públicas permitidas
                            .anyRequest().authenticated() // el resto requiere login
                    )
                    .httpBasic(Customizer.withDefaults()); // añade auth básica
            return http.build();
        }
    }


    // Caso de prueba principal:
    // Comprueba que el endpoint /api/public/buscar devuelve correctamente
    // la estructura esperada de resultados (artista, álbum, canción)
    // en JSON.
    @Test
    void buscar_okEstructuraBasica() throws Exception {

        // Datos simulados devueltos por el servicio
        var resultados = List.of(
                new BusquedaDTO(1L, "artista", "Aitana", null),
                new BusquedaDTO(2L, "album", "Cuarto azul", "Aitana"),
                new BusquedaDTO(3L, "cancion", "Super Estrella", "Aitana")
        );

        // Simulamos que el servicio devuelve estos resultados cuando se busca "Cuarto azul"
        when(buscarService.buscar(anyString())).thenReturn(resultados);

        // Simulamos una petición GET al endpoint con parámetros q, page y size
        mvc.perform(get("/api/public/buscar")
                        .param("q", "Cuarto azul")
                        .param("page", "0")
                        .param("size", "10"))

                // Esperamos un código 200 OK
                .andExpect(status().isOk())
                // El contenido debe ser JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validamos el contenido del JSON (tipo de cada resultado)
                .andExpect(jsonPath("$[0].tipo").value("artista"))
                .andExpect(jsonPath("$[1].tipo").value("album"))
                .andExpect(jsonPath("$[2].tipo").value("cancion"));
    }
}
