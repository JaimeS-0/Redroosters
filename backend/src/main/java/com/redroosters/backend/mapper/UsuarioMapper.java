package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.RegistroRequestDTO;
import com.redroosters.backend.dto.UsuarioResponseDTO;
import com.redroosters.backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Crear Usuarios nuevos
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "password", ignore = true) // la seteamos manualmente
    Usuario toEntity(RegistroRequestDTO dto);

    // Mapea de Usuario a UsuarioResponseDTO (para mostrar datos de usuario)
    UsuarioResponseDTO toDto(Usuario usuario);
}
