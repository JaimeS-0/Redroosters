package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.model.Artista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "canciones", ignore = true) // 👈 no intentes mapear esto
    @Mapping(target = "albumes", ignore = true)
    Artista toEntity(ArtistaRequestDTO dto);

    ArtistaResponseDTO toDTO(Artista artista);

    List<ArtistaResponseDTO> toDToList(List<Artista> artistas);
}
