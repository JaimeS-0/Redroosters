package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.*;
import com.redroosters.backend.model.MensajeContacto;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface MensajeContactoMapper {

    MensajeContactoResponseDTO toDto(MensajeContacto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    MensajeContacto toEntity(MensajeContactoRequestDTO dto);
}

