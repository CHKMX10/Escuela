package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.datos.DatosAula;
import com.jesus.escuela.dtos.datos.DatosCalificacion;
import com.jesus.escuela.dtos.datos.DatosCurso;
import com.jesus.escuela.dtos.datos.DatosMaestro;
import com.jesus.escuela.dtos.grupos.GrupoRequest;
import com.jesus.escuela.dtos.grupos.GrupoResponse;
import com.jesus.escuela.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupos>{

    private final CursoMapper cursoMapper;

    public GrupoMapper(CursoMapper cursoMapper) {
        this.cursoMapper = cursoMapper;
    }

    @Override
    public Grupos requestAEntidad(GrupoRequest request) {
        return null;
    }

    public Grupos requestAEntidad(GrupoRequest request, Cursos curso, Maestros maestro, Aulas aula) {
        return request == null
                ? null
                : Grupos.crear(
                        curso,
                        maestro,
                        aula,
                        request.periodo());
    }

    @Override
    public GrupoResponse entidadAResponse(Grupos entidad) {
        return entidad == null
                ? null
                : new GrupoResponse(
                entidad.getId(),
                cursoMapper.entidadADatosCurso(entidad.getCursos()),
                entidadADatosMaestro(entidad.getMaestro()),
                entidadADatosAula(entidad.getAulas()),
                entidadAHorarios(entidad),
                entidad.getPeriodo()
        );
    }
    private DatosMaestro entidadADatosMaestro(Maestros maestro) {
        return maestro == null
                ? null
                : new DatosMaestro(
                maestro.obtenerNombreCompleto(),
                maestro.getEmail(),
                maestro.getTelefono());
    }

    private DatosAula entidadADatosAula(Aulas aula) {
        return aula == null
                ? null
                : new DatosAula(aula.getNombre(), aula.getCapacidad());
    }

    private List<String> entidadAHorarios(Grupos entidad) {
        if (entidad.getHorarios() == null || entidad.getHorarios().isEmpty())
            return List.of();

        return entidad.getHorarios().stream()
                .map(this::horarioATexto)
                .toList();
    }

    private String horarioATexto(Horarios h) {
        return h.getDiaSem().getDescripcion() + " " + h.getHoraInicio() + " - " + h.getHoraFin();
    }
}
