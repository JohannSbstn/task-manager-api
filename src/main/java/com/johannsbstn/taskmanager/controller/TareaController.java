package com.johannsbstn.taskmanager.controller;

import com.johannsbstn.taskmanager.dto.TareaCreateDTO;
import com.johannsbstn.taskmanager.dto.TareaResponseDTO;
import com.johannsbstn.taskmanager.dto.TareaUpdateDTO;
import com.johannsbstn.taskmanager.model.EstadoTarea;
import com.johannsbstn.taskmanager.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "Endpoints para gestionar el ciclo de vida de las tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public ResponseEntity<List<TareaResponseDTO>> listar(@RequestParam(required = false) EstadoTarea estado) {
        List<TareaResponseDTO> tareas = tareaService.listarTareas(estado);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(@PathVariable Long id) {
        TareaResponseDTO tarea = tareaService.obtenerTareaPorId(id);
        return ResponseEntity.ok(tarea);
    }

    @Operation(summary = "Crear una nueva tarea", description = "Registra una tarea en el sistema con estado PENDIENTE " +
            "por defecto")
    @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaCreateDTO dto) {
        TareaResponseDTO tareaCreada = tareaService.crearTarea(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tareaCreada.id())
                .toUri();
        return ResponseEntity.created(uri).body(tareaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TareaUpdateDTO dto) {
        TareaResponseDTO tareaActualizar = tareaService.actualizarTarea(id, dto);
        return ResponseEntity.ok(tareaActualizar);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TareaResponseDTO> cambiarEstado(@PathVariable Long id, @RequestParam EstadoTarea estado) {
        TareaResponseDTO estadoActualizar = tareaService.cambiarEstadoTarea(id, estado);
        return ResponseEntity.ok(estadoActualizar);
    }

    @Operation(summary = "Eliminar una tarea", description = "Elimina una tarea existente en el sistema")
    @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente")
    @ApiResponse(responseCode = "400", description = "Tarea no encontrada")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
    }
}
