package com.redroosters.backend.mapper;

import com.redroosters.backend.dto.EscuchaResponseDTO;
import com.redroosters.backend.dto.TopCancionDTO;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.model.Escucha;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// Para convertir entre DTOs y la entidad Escucha

@Mapper(componentModel = "spring")
public interface EscuchaMapper {

    @Mapping(target = "nombreUsuario", source = "usuario.name")
    @Mapping(target = "tituloCancion", source = "cancion.titulo")
    EscuchaResponseDTO toDto(Escucha escucha);

    List<EscuchaResponseDTO> toDtoList(List<Escucha> escuchas);

    // Para el TOP global
    @Mapping(target = "cancionId", source = "cancion.id")
    @Mapping(target = "titulo", source = "cancion.titulo")
    @Mapping(target = "artista", source = "cancion.artista.nombre")
    @Mapping(target = "portadaUrl", source = "cancion.portadaUrl")
    @Mapping(target = "escuchasTotales", source = "total")
    TopCancionDTO toTopDto(Cancion cancion, Long total);
}


