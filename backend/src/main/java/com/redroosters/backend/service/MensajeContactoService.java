package com.redroosters.backend.service;

import com.redroosters.backend.dto.MensajeContactoRequestDTO;
import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import com.redroosters.backend.exception.MensajeContactoNotFoundException;
import com.redroosters.backend.mapper.MensajeContactoMapper;
import com.redroosters.backend.model.MensajeContacto;
import com.redroosters.backend.repository.MensajeContactoRepository;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

// Logica de CRUD y visualizar Mensaje conatacto

@Service
public class MensajeContactoService {

    private final MensajeContactoRepository mensajeContactoRepository;
    private final MensajeContactoMapper mensajeContactoMapper;

    public MensajeContactoService(MensajeContactoRepository mensajeContactoRepository, MensajeContactoMapper mensajeContactoMapper) {
        this.mensajeContactoRepository = mensajeContactoRepository;
        this.mensajeContactoMapper = mensajeContactoMapper;
    }

    // Guardar un nuevo mensake enviado desde el formulario de contacto
    // Publico USER
    public MensajeContactoResponseDTO crearMensaje(MensajeContactoRequestDTO dto) {
        MensajeContacto entidad = mensajeContactoMapper.toEntity(dto);
        MensajeContacto guardado = mensajeContactoRepository.save(entidad);
        return mensajeContactoMapper.toDto(guardado);
    }

    // Listar mensajes recibidos en el panel de administracion
    // Privado ADMIN
    public List<MensajeContactoResponseDTO> listarMensajes() {
        List<MensajeContacto> mensajes = mensajeContactoRepository.findAll();
        return mensajes.stream()
                .map(mensajeContactoMapper::toDto)
                .toList();
    }

    // Ver un mensaje concreto y completo por id
    // Privado ADMIN
    public MensajeContactoResponseDTO getMensajeId(Long id) {
        MensajeContacto entidad = mensajeContactoRepository.findById(id)
                .orElseThrow(() -> new MensajeContactoNotFoundException(id));
        return mensajeContactoMapper.toDto(entidad);
    }

}
