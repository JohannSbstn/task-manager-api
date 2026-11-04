package com.johannsbstn.taskmanager.service;

import com.johannsbstn.taskmanager.dto.TareaCreateDTO;
import com.johannsbstn.taskmanager.dto.TareaResponseDTO;
import com.johannsbstn.taskmanager.dto.TareaUpdateDTO;
import com.johannsbstn.taskmanager.exception.ResourceNotFoundException;
import com.johannsbstn.taskmanager.model.EstadoTarea;
import com.johannsbstn.taskmanager.model.Tarea;
import com.johannsbstn.taskmanager.repository.TareaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDTO> listarTareas(EstadoTarea estado) {
        List<Tarea> tareas;
        if (estado == null) {
            tareas = tareaRepository.findAll();
        } else  {
            tareas = tareaRepository.findByEstado(estado);
        }

        return tareas.stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TareaResponseDTO obtenerTareaPorId(Long id) {
        Tarea tarea = buscarTareaPorId(id);

        return mapearAResponse(tarea);
    }

    @Transactional
    public TareaResponseDTO crearTarea(TareaCreateDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setFechaLimite(dto.fechaLimite());

        Tarea tareaGuardada = tareaRepository.save(tarea);
        return mapearAResponse(tareaGuardada);
    }

    @Transactional
    public TareaResponseDTO actualizarTarea(Long id, TareaUpdateDTO dto) {
        Tarea tarea = buscarTareaPorId(id);
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setFechaLimite(dto.fechaLimite());

        Tarea tareaActualizada = tareaRepository.save(tarea);
        return mapearAResponse(tareaActualizada);
    }

    @Transactional
    public void eliminarTarea(Long id) {
        Tarea tarea = buscarTareaPorId(id);
        tareaRepository.delete(tarea);
    }

    @Transactional
    public TareaResponseDTO cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = buscarTareaPorId(id);
        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);
        return mapearAResponse(tarea);
    }

    private TareaResponseDTO mapearAResponse(Tarea tarea) {
        return new TareaResponseDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getFechaCreacion(),
                tarea.getFechaLimite()
        );
    }

    private Tarea buscarTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tarea no encontrada")
                );
    }
}
