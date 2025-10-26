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

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                // Evita que Spring cree tu seguridad real (y así no necesita CustomUserDetailsService)
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.redroosters.backend.security.JwtAuthenticationFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false) // sin filtros de seguridad en este test
class AuthControllerTest {

    @Autowired MockMvc mvc;

    @MockitoBean AuthenticationManager authenticationManager;
    @MockitoBean JwtService jwtService;
    @MockitoBean UsuarioRepository usuarioRepository;
    @MockitoBean UsuarioService usuarioService;

    @Test
    void login_ok() throws Exception {
        var body = """
      {"email":"user@demo.com","password":"123456789"}
    """;

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@demo.com","1234"));

        var u = new Usuario();
        u.setEmail("user@demo.com");
        u.setName("Demo");
        u.setRole(Role.USER);
        when(usuarioRepository.findByEmail("user@demo.com")).thenReturn(Optional.of(u));

        when(jwtService.generateToken("user@demo.com")).thenReturn("FAKE_JWT");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("FAKE_JWT")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("user@demo.com")));
    }

    @Test
    void register_ok() throws Exception {
        var body = """
      {"name":"Jaime","email":"jaime@demo.com","password":"123456789"}
    """;

        // si quieres, devuelve un DTO real; aquí basta con comprobar 200
        when(usuarioService.registrar(any(), anyString(), anyString())).thenReturn(null);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}
