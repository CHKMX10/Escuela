package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Grupos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupos, Long> {

    boolean existsByMaestroId(Long id);//esto es para poder eliminar, al verificar si eñ maestro tiene un curso, si lo tiene no se puede eliminar
}
