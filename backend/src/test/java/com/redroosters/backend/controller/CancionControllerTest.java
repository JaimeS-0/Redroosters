package com.redroosters.backend.controller;

import com.redroosters.backend.controller.admin.CancionController;
import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.service.CancionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Pruebas unitarias del controlador de canciones (CancionController)
// Endpoint de creacion con archivo

// Se usa @WebMvcTest para cargar solo la capa web (sin servicios reales ni base de datos)

@WebMvcTest(
        controllers = CancionController.class,
        excludeFilters = {
                // Se excluye la configuracion real de seguridad y el filtro JWT
                // ya que aquí solo se prueba el comportamiento del controllerra
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.JwtAuthenticationFilter.class)
        }
)
@Import(CancionControllerTest.TestSecurityConfig.class)
class CancionControllerTest {

    @Autowired MockMvc mvc; // Permite simulaar peticiones HTTP al controlador

    @MockitoBean
    CancionService cancionService;


    // Configuración de seguridad mínima para los tests
    // Permite controlar qué rutas requieren autenticación (simulando roles)
    // sin cargar la seguridad real con JWT
    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll()
                    )
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }
    }


    // Prueba 1: Se intenta crear una cancion sin estar autenticado
    // Al no enviar contraseñas ni token, debe devolver 401 (Unauthorized)
    @Test
    void crearSinToken_devuelveUnauthorized() throws Exception {
        mvc.perform(multipart("/api/admin/cancion/archivo"))
                .andExpect(status().isUnauthorized());
    }


    // Prueba 2: Usuario autenticado con rol ADMIN crea una canción correctamente
    // Se mockea la respuesta del servicio y se valida que el endpoint devuelva 201 y los datos esperados
    @Test
    @WithMockUser(roles = "ADMIN")
    void crearConMultipart_okCreated() throws Exception {

        // Archivos simulados enviados en la petición (audio + datos JSON)
        var audio = new MockMultipartFile("audio","tema.mp3","audio/mpeg","FAKE".getBytes());
        var datos = new MockMultipartFile("datos","", "application/json",
                "{\"titulo\":\"Tema\",\"artistaId\":1,\"albumId\":1}".getBytes());

        // Se simula la respuesta del servicio cuando se guarda una canción
        when(cancionService.crearConArchivo(any(CancionRequestDTO.class), any(), any()))
                .thenReturn(new CancionResponseDTO(
                        10L, "Lia", "Artista Famosa y Guapa", 215, "2:30",
                        "/audios/tema.mp3", "/audios/tema.mp3", "Aitana", "Cuarto Azul", 1L, 1L, 1L, 2L
                ));

        // Se realiza la petición HTTP POST multipart simulada y se comprueba la respuesta
        mvc.perform(multipart("/api/admin/cancion/archivo")
                        .file(audio).file(datos)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated()) // 201 CREATED
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titulo").value("Lia"))
                .andExpect(jsonPath("$.descripcion").value("Artista Famosa y Guapa"))
                .andExpect(jsonPath("$.duracionSegundos").value(215))
                .andExpect(jsonPath("$.duracionTexto").value("2:30"))
                .andExpect(jsonPath("$.nombreArtista").value("Aitana"))
                .andExpect(jsonPath("$.tituloAlbum").value("Cuarto Azul"));
    }
}
