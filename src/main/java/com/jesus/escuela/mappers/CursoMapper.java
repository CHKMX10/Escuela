package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.cursos.CursoRequest;
import com.jesus.escuela.dtos.cursos.CursoResponse;
import com.jesus.escuela.dtos.datos.DatosCurso;
import com.jesus.escuela.entities.Cursos;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Cursos>{
    @Override
    public Cursos requestAEntidad(CursoRequest request) {
        return null;
    }

    @Override
    public CursoResponse entidadAResponse(Cursos entidad) {
        return null;
    }

    public DatosCurso entidadADatosCurso(Cursos entidad){
        return entidad == null
                ? null
                : new DatosCurso(
                        entidad.getNombre(),
                        entidad.getDescripcion(),
                        entidad.getCreditos());

    }
}
