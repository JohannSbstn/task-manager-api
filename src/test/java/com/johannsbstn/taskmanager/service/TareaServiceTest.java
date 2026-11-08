package com.johannsbstn.taskmanager.service;

import com.johannsbstn.taskmanager.dto.TareaCreateDTO;
import com.johannsbstn.taskmanager.dto.TareaResponseDTO;
import com.johannsbstn.taskmanager.exception.ResourceNotFoundException;
import com.johannsbstn.taskmanager.model.EstadoTarea;
import com.johannsbstn.taskmanager.model.Tarea;
import com.johannsbstn.taskmanager.repository.TareaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @Test
    @DisplayName("Debe obtener una tarea por ID correctamente")
    void obtenerTareaPorId_cuandoExiste_retornaDto() {
        Long id = 1L;
        Tarea tareaMock = new Tarea();
        tareaMock.setId(id);
        tareaMock.setTitulo("Test Tarea");
        tareaMock.setEstado(EstadoTarea.PENDIENTE);

        when(tareaRepository.findById(id)).thenReturn(Optional.of(tareaMock));

        TareaResponseDTO resultado = tareaService.obtenerTareaPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Test Tarea", resultado.titulo());

        verify(tareaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la tarea no existe")
    void obtenerTareaPorId_cuandoNoExiste_lanzaExcepcion() {
        Long id = 99L;

        when(tareaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tareaService.obtenerTareaPorId(id));
    }

    @Test
    @DisplayName("Debe crear una tarea correctamente")
    void crearTarea_retornaDtoCreado() {
        TareaCreateDTO dto = new TareaCreateDTO("Nueva Tarea", "Descripción", null);

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setId(1L);
        tareaGuardada.setTitulo(dto.titulo());
        tareaGuardada.setDescripcion(dto.descripcion());
        tareaGuardada.setEstado(EstadoTarea.PENDIENTE);

        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardada);

        TareaResponseDTO resultado = tareaService.crearTarea(dto);

        assertNotNull(resultado.id());
        assertEquals(dto.titulo(), resultado.titulo());
        assertEquals(EstadoTarea.PENDIENTE, resultado.estado());
    }

    @Test
    @DisplayName("Debe eliminar una tarea correctamente")
    void eliminarTarea_cuandoExiste_llamaDelete() {
        // Arrange
        Long id = 1L;
        Tarea tareaMock = new Tarea();
        tareaMock.setId(id);

        when(tareaRepository.findById(id)).thenReturn(Optional.of(tareaMock));

        tareaService.eliminarTarea(id);

        verify(tareaRepository, times(1)).delete(tareaMock);
    }
}
