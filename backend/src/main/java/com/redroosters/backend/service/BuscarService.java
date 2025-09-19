package com.redroosters.backend.service;

import com.redroosters.backend.dto.BusquedaDTO;
import com.redroosters.backend.repository.BuscarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Recibe los parametros de busqueda y los devuelve a la web para mostrar los resultados del bsucador.

@Service
public class BuscarService {

    private final BuscarRepository buscar;

    public BuscarService(BuscarRepository buscar) {
        this.buscar = buscar;
    }

    public List<BusquedaDTO> buscar(String q, int page, int size) {
        int p = Math.max(0, page);
        int s = Math.max(1, size);
        int offset = p * s;
        return buscar.buscarTodo(q, s, offset);
    }
}
