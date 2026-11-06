package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Calificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionesRepository extends JpaRepository<Calificaciones, Long> {

    boolean existsByInscripcionId(Long idInscripcion);

    boolean existsByInscripcionIdAndIdNot(Long idInscripcion, Long idCalificacion);

}
