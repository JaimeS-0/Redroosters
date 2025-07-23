package com.redroosters.backend.service;

import com.redroosters.backend.dto.RegistroRequestDTO;
import com.redroosters.backend.dto.UsuarioResponseDTO;
import com.redroosters.backend.exception.EmailAlreadyExistsException;
import com.redroosters.backend.exception.UsernameAlreadyExistsException;
import com.redroosters.backend.mapper.UsuarioMapper;
import com.redroosters.backend.model.Role;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un nuevo Usuario con rol USER y password encriprtada

    public UsuarioResponseDTO registrar(RegistroRequestDTO dto, String username, String email) {

        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException(username);
        }

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(email);
        }

        Usuario nuevo = usuarioMapper.toEntity(dto);
        nuevo.setPassword(passwordEncoder.encode(dto.password()));
        nuevo.setRole(Role.USER); // Por defecto
        Usuario guardado = usuarioRepository.save(nuevo);

        return new UsuarioResponseDTO(
                guardado.getId(),
                guardado.getUsername(),
                guardado.getEmail(),
                guardado.getRole()
        );

    }
}
