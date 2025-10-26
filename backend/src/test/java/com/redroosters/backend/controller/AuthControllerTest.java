package com.redroosters.backend.controller;

import com.redroosters.backend.model.Role;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.UsuarioRepository;
import com.redroosters.backend.security.JwtService;
import com.redroosters.backend.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Pruebas unitarias del AuthController con slice Web MVC
// Se mockean TODAS las dependencias (AuthenticationManager, JwtService, Repositorio y Servicio)
// para probar el comportamiento del controlador en aislamiento sin seguridad real ni BBDD
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                // Excluimos la seguridad real para evitar cargar filtros/JWT/usuarios reales
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.JwtAuthenticationFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false) // Desactiva cualquier filtro de seguridad que quedara activo
class AuthControllerTest {

    @Autowired MockMvc mvc; // Cliente MVC simulado para invocar endpoints

    // Dependencias del controlador mockeadas con Mockito
    @MockitoBean AuthenticationManager authenticationManager; // Autenticación de Spring
    @MockitoBean JwtService jwtService;                        // Generación de tokens
    @MockitoBean UsuarioRepository usuarioRepository;          // Acceso a usuarios
    @MockitoBean UsuarioService usuarioService;                // Lógica de registro


    // Caso de login
    // Simulamos que AuthenticationManager autentica correctamente
    // Simulamos que existe el usuario en la BBDD
    // Simulamos que JwtService genera un token
    // Verificamos que el endpoint responde 200 OK y contiene el token y el email
    @Test
    void login_ok() throws Exception {

        // Body JSON enviado por el cliente
        var body = """
          {"email":"user@demo.com","password":"123456789"}
        """;

        // El AuthenticationManager acepta las credenciales
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@demo.com","1234"));

        // El repositorio encuentra al usuario por email
        var u = new Usuario();
        u.setEmail("user@demo.com");
        u.setName("Demo");
        u.setRole(Role.USER);
        when(usuarioRepository.findByEmail("user@demo.com")).thenReturn(Optional.of(u));

        // El servicio JWT genera un token simulado
        when(jwtService.generateToken("user@demo.com")).thenReturn("FAKE_JWT");

        // Ejecutamos la petición y comprobamos respuesta y contenido
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("FAKE_JWT")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("user@demo.com")));
    }


    // Caso de registro
    // Simulamos que el servicio de usuarios registra bien y no lanza errores
    // Verificar el 200 OK
    @Test
    void register_ok() throws Exception {
        var body = """
          {"name":"Jaime","email":"jaime@demo.com","password":"123456789"}
        """;

        // Simulamos que registrar() funciona
        when(usuarioService.registrar(any(), anyString(), anyString())).thenReturn(null);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}
