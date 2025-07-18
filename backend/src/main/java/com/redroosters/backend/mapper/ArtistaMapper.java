package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.ArtistaRequestDTO;
import com.redroosters.backend.dto.ArtistaResponseDTO;
import com.redroosters.backend.model.Artista;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistaMapper {

    Artista toEntity(ArtistaRequestDTO dto);

    ArtistaResponseDTO toDTO(Artista artista);

    List<ArtistaResponseDTO> toDToList(List<Artista> artistas);
}
