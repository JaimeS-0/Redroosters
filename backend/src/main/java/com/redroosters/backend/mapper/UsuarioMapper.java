package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.UsuarioResponseDTO;
import com.redroosters.backend.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toDto(Usuario usuario);
}
