package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CursoRepository extends JpaRepository<Cursos, Long> {

    Optional<Cursos> findByNombre(String nombre);

    boolean existsById(Long Id);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
