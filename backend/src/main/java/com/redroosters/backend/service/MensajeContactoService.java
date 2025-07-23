package com.redroosters.backend.service;

import com.redroosters.backend.dto.MensajeContactoRequestDTO;
import com.redroosters.backend.dto.MensajeContactoResponseDTO;
import com.redroosters.backend.mapper.MensajeContactoMapper;
import com.redroosters.backend.model.MensajeContacto;
import com.redroosters.backend.repository.MensajeContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
