package com.redroosters.backend.config;

import com.redroosters.backend.model.Role;
import com.redroosters.backend.model.Usuario;
import com.redroosters.backend.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// Creacion del usuario ADMIN

@Configuration
public class AdminConfig {

    private static final Logger log = LoggerFactory.getLogger(AdminConfig.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminConfig(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    void initData() {

        if (usuarioRepository.existsByEmail(adminEmail)) {
            System.out.println("Admin ya existe no se puede crear uno nuevo");
            return;
        }

        // Creamos el usuario ADMIN
        Usuario admin = new Usuario();
        admin.setName(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        usuarioRepository.save(admin);

        log.info(" ✅ Usuario ADMIN creado correctamente + " +
                "Nombre: " + adminUsername +
                "Email: " + adminEmail +
                "Password: " + adminPassword +
                "Rol: " + admin.getRole() + " ✅ ");
    }
}
