package com.johannsbstn.taskmanager.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TareaUpdateDTO(
        @NotBlank
        @Size(max = 100)
        String titulo,
        @Size(max = 1000)
        String descripcion,
        @FutureOrPresent
        LocalDateTime fechaLimite
) {}
