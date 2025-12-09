package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.AlbumRequestDTO;
import com.redroosters.backend.dto.AlbumResponseDTO;
import com.redroosters.backend.model.Album;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// Para convertir entre DTOs y la entidad Album

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artista", ignore = true)
    @Mapping(target = "canciones", ignore = true)
    Album toEntity(AlbumRequestDTO dto);

    @Mapping(target = "nombreArtista", source = "artista.nombre")
    @Mapping(
            target = "canciones",
            expression = "java( album.getCanciones().stream()" +
                    ".map(c -> new com.redroosters.backend.dto.AlbumCancionDTO(c.getId(), c.getTitulo()))" +
                    ".toList() )"
    )
    @Mapping(target = "artistaId", source = "artista.id")
    AlbumResponseDTO toDto(Album album);

    List<AlbumResponseDTO> toDtoList(List<Album> albums);
}
