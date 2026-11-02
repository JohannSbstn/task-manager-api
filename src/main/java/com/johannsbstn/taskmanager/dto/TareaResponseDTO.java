package com.johannsbstn.taskmanager.dto;

import com.johannsbstn.taskmanager.model.EstadoTarea;

import java.time.LocalDateTime;

public record TareaResponseDTO(
        Long id,
        String titulo,
        String descripcion,
        EstadoTarea estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaLimite
) {}
