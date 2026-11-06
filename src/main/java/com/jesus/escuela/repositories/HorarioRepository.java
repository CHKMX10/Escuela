package com.jesus.escuela.repositories;

import com.jesus.escuela.entities.Horarios;
import com.jesus.escuela.enums.DiaSem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horarios, Long> {

    boolean existsByGrupoId(Long id);
    @Query("""
        SELECT h
        FROM Horarios h
        WHERE h.grupo.id = :grupoId
          AND h.diaSem = :diaSem
          AND h.horaInicio < :horaFin
          AND h.horaFin > :horaInicio
          AND (:idExcluir IS NULL OR h.id <> :idExcluir)
        """)
    List<Horarios> buscarTraslapesPorGrupo(
            @Param("grupoId") Long grupoId,
            @Param("diaSem") DiaSem diaSem,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluir") Long idExcluir
    );

    // Traslapes en la misma aula y periodo, pero en otro grupo.
    @Query("""
        SELECT h
        FROM Horarios h
        WHERE h.grupo.aulas.id = :aulaId
          AND h.grupo.periodo = :periodo
          AND h.grupo.id <> :grupoId
          AND h.diaSem = :diaSem
          AND h.horaInicio < :horaFin
          AND h.horaFin > :horaInicio
          AND (:idExcluir IS NULL OR h.id <> :idExcluir)
        """)
    List<Horarios> buscarTraslapesPorAulaYPeriodo(
            @Param("aulaId") Long aulaId,
            @Param("periodo") String periodo,
            @Param("grupoId") Long grupoId,
            @Param("diaSem") DiaSem diaSem,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluir") Long idExcluir
    );
}
