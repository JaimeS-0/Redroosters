package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.model.Cancion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CancionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artista", ignore = true)
    @Mapping(target = "album", ignore = true)
    Cancion toEntity(CancionRequestDTO dto);

    @Mapping(target = "nombreArtista", source = "artista.nombre")
    @Mapping(target = "tituloAlbum", source = "album.titulo")
    CancionResponseDTO toDto(Cancion cancion);

    List<CancionResponseDTO> toDtoList(List<Cancion> canciones);
}

