package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.EscuchaRequestDTO;
import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.model.Escucha;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EscuchaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cancion", ignore = true)
    @Mapping(target = "vecesEscuchada", constant = "1")
    @Mapping(target = "ultimaEscucha", expression = "java(java.time.LocalDateTime.now())")
    Escucha toEntity(EscuchaRequestDTO dto);

    @Mapping(target = "nombreUsuario", source = "usuario.username")
    @Mapping(target = "tituloCancion", source = "cancion.titulo")
    EscuchaResponseDTO toDto(Escucha escucha);

    List<EscuchaResponseDTO> toDtoList(List<Escucha> lista);
}

