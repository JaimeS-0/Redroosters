package com.redroosters.backend.repository;

import com.redroosters.backend.dto.BusquedaDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// Repositio JPA para la busqueda  y consultas personalizadas

@Repository
public class BuscarRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public BuscarRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Define cómo convertir cada fila del SQL en un BusquedaDTO
    private static final RowMapper<BusquedaDTO> ROW_MAPPER = new RowMapper<>() {
        @Override
        public BusquedaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BusquedaDTO(
                    rs.getLong("id"),
                    rs.getString("tipo"),
                    rs.getString("titulo"),
                    rs.getString("subtitulo")
            );
        }
    };

    public List<BusquedaDTO> buscarTodo(String q) {

        String sql = """
            WITH q AS (SELECT :term AS term)
            
            SELECT a.id AS id,
                   'ARTISTA' AS tipo,
                   a.nombre AS titulo,
                   NULL AS subtitulo,
                   1.0::float8 AS score
            FROM artistas a, q
            WHERE a.nombre ILIKE '%%' || q.term || '%%'

            UNION ALL

            SELECT c.id AS id,
                   'CANCION' AS tipo,
                   c.titulo AS titulo,
                   ar.nombre AS subtitulo,
                   0.8::float8 AS score
            FROM canciones c
            JOIN artistas ar ON ar.id = c.artista_id, q
            WHERE c.titulo ILIKE '%%' || q.term || '%%'
               OR ar.nombre ILIKE '%%' || q.term || '%%'

            UNION ALL

            SELECT al.id AS id,
                   'ALBUM' AS tipo,
                   al.titulo AS titulo,
                   ar.nombre AS subtitulo,
                   0.7::float8 AS score
            FROM albumes al
            JOIN artistas ar ON ar.id = al.artista_id, q
            WHERE al.titulo ILIKE '%%' || q.term || '%%'
               OR ar.nombre ILIKE '%%' || q.term || '%%'

            ORDER BY score DESC, titulo ASC
            """;

        return jdbc.query(
                sql,
                Map.of("term", q == null ? "" : q.trim()),
                ROW_MAPPER
        );


    // Busqueda de Artistas, Albumes y Canciones a la vez

    // q -> lo que el usuario escribe en el buscador
    // limit -> resultados maximos
    // offset -> desde donde empezar
    // Guarda el parametro :term en una tabla temporal ppara utilizrlo en todas las busquedas

    // Justamos todos los resultados (UNION ALL),
    // Los ordenamos primero por score(Canciones, Albumes, Artistas) y alfanbeticamente
    /*
    public List<BusquedaDTO> buscarTodo(String q, int limit, int offset) {

        String sql = """
            WITH q AS (SELECT :term AS term)
            SELECT a.id AS id,
                   'ARTISTA' AS tipo,
                   a.nombre AS titulo,
                   NULL AS subtitulo,
                   1.0::float8 AS score
            FROM artistas a, q
            WHERE a.nombre ILIKE '%%' || q.term || '%%'

            UNION ALL

            SELECT c.id AS id,
                   'CANCION' AS tipo,
                   c.titulo AS titulo,
                   ar.nombre AS subtitulo,
                   0.7::float8 AS score
            FROM canciones c
            JOIN artistas ar ON ar.id = c.artista_id, q
            WHERE c.titulo ILIKE '%%' || q.term || '%%'
               OR ar.nombre ILIKE '%%' || q.term || '%%'

            UNION ALL

            SELECT al.id AS id,
                   'ALBUM' AS tipo,
                   al.titulo AS titulo,
                   ar.nombre AS subtitulo,
                   0.8::float8 AS score
            FROM albumes al
            JOIN artistas ar ON ar.id = al.artista_id, q
            WHERE al.titulo ILIKE '%%' || q.term || '%%'
               OR ar.nombre ILIKE '%%' || q.term || '%%'

            ORDER BY score DESC, titulo ASC
            LIMIT :limit OFFSET :offset
            """;

        return jdbc.query(
                sql,
                Map.of(
                        "term", q == null ? "" : q.trim(),
                        "limit", Math.max(1, limit),
                        "offset", Math.max(0, offset)
                ),
                ROW_MAPPER
        );*/
    }
}
