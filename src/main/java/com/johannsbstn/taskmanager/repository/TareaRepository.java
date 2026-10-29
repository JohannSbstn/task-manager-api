package com.johannsbstn.taskmanager.repository;

import com.johannsbstn.taskmanager.model.EstadoTarea;
import com.johannsbstn.taskmanager.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByEstado(EstadoTarea estadoTarea);

    List<Tarea> findByTituloContainingIgnoreCase(String texto);
}
