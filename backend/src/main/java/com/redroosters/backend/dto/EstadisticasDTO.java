package com.redroosters.backend.dto;

// Se usa para mostrar todas las estadisticas en el panel de administracion

public record EstadisticasDTO (

        long totalCanciones,
        long totalArtistas,
        long totalAlbumes,
        long totalEscuchasGlobales,
        EstadisticaEscuchaLikeDTO cancionMasEscuchada,
        EstadisticaEscuchaLikeDTO cancionConMasLikes

){}
