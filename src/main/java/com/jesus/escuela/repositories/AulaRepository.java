package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Aulas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aulas , Long> {

    Optional<Aulas> findByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre); //

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id); //

}
