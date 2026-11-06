package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.calificaciones.CalificacionResponse;
import com.jesus.escuela.dtos.calificaciones.CalificacionesRequest;
import com.jesus.escuela.dtos.datos.DatosInscripcionCalificacion;
import com.jesus.escuela.entities.Calificaciones;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

@Component
public class CalificacionesMapper implements CommonMapper<CalificacionesRequest, CalificacionResponse, Calificaciones> {
    public Calificaciones requestAEntidad(CalificacionesRequest request, Inscripcion inscripcion) {
        if (request == null || inscripcion == null) {
            return null;
        }
        return Calificaciones.crear(inscripcion, request.calificacion());
    }

    @Override
    public Calificaciones requestAEntidad(CalificacionesRequest request) {
        return null;
    }

    @Override
    public CalificacionResponse entidadAResponse(Calificaciones entidad) {
        if (entidad == null) {
            return null;
        }

        Inscripcion inscripcion = entidad.getInscripcion();

        DatosInscripcionCalificacion datosInscripcion =
                new DatosInscripcionCalificacion(
                        inscripcion.getAlumno().obtenerNombreCompleto(),
                        inscripcion.getGrupo().getCursos().getNombre(),
                        inscripcion.getGrupo().getPeriodo()
                );

        return new CalificacionResponse(
                entidad.getId(),
                datosInscripcion,
                entidad.getCalificacion(),
                StringCustomUtils.localDateAString(entidad.getFechaIngreso())
        );
    }
}

