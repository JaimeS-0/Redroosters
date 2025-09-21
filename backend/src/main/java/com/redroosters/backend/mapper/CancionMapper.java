package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.CancionRequestDTO;
import com.redroosters.backend.dto.CancionResponseDTO;
import com.redroosters.backend.model.Cancion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

// Para convertir entre DTOs y la entidad Cancion

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = { com.redroosters.backend.utils.AudioUtils.class }
)
public interface CancionMapper {

    @Mapping(target = "nombreArtista", source = "artista.nombre")
    @Mapping(target = "tituloAlbum", source = "album.titulo")
    @Mapping(
            target = "duracionTexto",
            expression = "java(c.getDuracionSegundos() != null ? AudioUtils.formatMmSs(c.getDuracionSegundos().intValue()) : null)"
    )
    CancionResponseDTO toDto(Cancion c);

    List<CancionResponseDTO> toDtoList(List<Cancion> canciones);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artista", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "duracionSegundos", ignore = true)
    @Mapping(target = "urlAudio", source = "urlAudio")
    Cancion toEntity(CancionRequestDTO dto);
}
