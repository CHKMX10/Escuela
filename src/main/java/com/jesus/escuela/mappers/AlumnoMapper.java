package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.alumnos.AlumnoRequest;
import com.jesus.escuela.dtos.alumnos.AlumnoResponse;
import com.jesus.escuela.dtos.datos.DatosCalificacion;
import com.jesus.escuela.entities.Alumnos;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumnos> {
    @Override
    public Alumnos requestAEntidad(AlumnoRequest request) {//no se le puede meter el email y la matricula
        return request == null
                ? null
                : Alumnos.crear(
                        request.nombre(),
                        request.apellidoPaterno(),
                        request.apellidoMaterno()
        );
    }
    public Alumnos requestAEntidad(AlumnoRequest request, String email, String matricula) {// tiene que hacer uso de la recursividad
        if(request == null) return null;

        Alumnos alumno = requestAEntidad(request);
        alumno.asignarDatosAcademicos(email, matricula);
        return alumno;
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumnos entidad) {
        return entidad == null
                ? null
                : new AlumnoResponse(
                entidad.getId(),
                entidad.obtenerNombreCompleto(),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.localDateAString(
                        entidad.getDateIngreso()),
                null,
                entidad.calcularPromedio());
    }

    private List<DatosCalificacion> entidadADatosCalificacion(Alumnos entidad){

        if(entidad == null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificacion(
                        inscripcion.getGrupo().getCursos().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificaciones() != null
                            ? inscripcion.getCalificaciones().getCalificacion()
                                : null))
                .toList();
    }

}
