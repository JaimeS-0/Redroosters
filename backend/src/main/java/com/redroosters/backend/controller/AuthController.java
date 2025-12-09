package com.redroosters.backend.controller;

import com.redroosters.backend.docs.AuthApi;
import com.redroosters.backend.dto.LoginRequestDTO;
import com.redroosters.backend.dto.LoginResponseDTO;
import com.redroosters.backend.dto.RegistroRequestDTO;
import com.redroosters.backend.dto.UsuarioResponseDTO;
import com.redroosters.backend.exception.UsuarioNotFoundException;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.UsuarioRepository;
import com.redroosters.backend.security.JwtService;
import com.redroosters.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


// Para loguearse en la app y registrarse

@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    // Iniciar sesión
    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {

        // Verifica email y password
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsuarioNotFoundException(request.email()));

        // Si rememberMe es null -> lo tratamos como false
        boolean rememberMe = Boolean.TRUE.equals(request.rememberMe());

        // Generar token con su email y rememberMe
        String token = jwtService.generateToken(usuario.getEmail(), rememberMe);

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                usuario.getName(),
                usuario.getEmail(),
                usuario.getRole().name()
        ));
    }


    // Registro de usuario
    @Override
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody @Valid RegistroRequestDTO request) {
        UsuarioResponseDTO registrado = usuarioService.registrar(
                request,
                request.name(),
                request.email()
        );
        return ResponseEntity.ok(registrado);
    }
}
