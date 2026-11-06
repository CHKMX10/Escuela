package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Grupos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupos, Long> {

    boolean existsByMaestroId(Long maestroId);

    boolean existsByAulasId(Long aulasId);

    boolean existsByCursosId(Long cursoId);

    boolean existsByCursosIdAndMaestroIdAndAulasIdAndPeriodo(
            Long cursoId, Long maestroId, Long aulaId, String periodo);

    boolean existsByCursosIdAndMaestroIdAndAulasIdAndPeriodoAndIdNot(
            Long cursoId, Long maestroId, Long aulaId, String periodo, Long id
    );
}
