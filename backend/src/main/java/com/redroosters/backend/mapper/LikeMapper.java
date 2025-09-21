package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.LikeRequestDTO;
import com.redroosters.backend.dto.LikeResponseDTO;
import com.redroosters.backend.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// Para convertir entre DTOs y la entidad Like

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cancion", ignore = true)
    @Mapping(target = "fecha", expression = "java(java.time.LocalDateTime.now())")
    Like toEntity(LikeRequestDTO dto);

    @Mapping(target = "nombreUsuario", source = "usuario.username")
    @Mapping(target = "tituloCancion", source = "cancion.titulo")
    LikeResponseDTO toDto(Like like);

    List<LikeResponseDTO> toDtoList(List<Like> lista);
}

