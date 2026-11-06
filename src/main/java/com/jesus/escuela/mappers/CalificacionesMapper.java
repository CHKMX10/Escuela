package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.calificaciones.CalificacionResponse;
import com.jesus.escuela.dtos.calificaciones.CalificacionesRequest;
import com.jesus.escuela.dtos.datos.DatosAlumnoInscripcion;
import com.jesus.escuela.dtos.datos.DatosGrupoHorario;
import com.jesus.escuela.dtos.datos.DatosInscripcionCalificacion;
import com.jesus.escuela.entities.Alumnos;
import com.jesus.escuela.entities.Calificaciones;
import com.jesus.escuela.entities.Grupos;
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

        Alumnos alumno = inscripcion.getAlumno();

        Grupos grupo = inscripcion.getGrupo();

        DatosAlumnoInscripcion datosAlumno = new DatosAlumnoInscripcion(
                alumno.obtenerNombreCompleto(),
                alumno.getMatricula(),
                alumno.getEmail(),
                StringCustomUtils.localDateAString(alumno.getDateIngreso())
        );

        DatosGrupoHorario datosGrupo = new DatosGrupoHorario(
                grupo.getCursos().getNombre(),
                grupo.getMaestro().obtenerNombreCompleto(),
                grupo.getAulas().getNombre(),
                grupo.getPeriodo()
        );

        DatosInscripcionCalificacion datosInscripcion =
                new DatosInscripcionCalificacion(
                        datosAlumno,
                        datosGrupo,
                        StringCustomUtils.localDateAString(
                                inscripcion.getFechaInscripcion()
                        )
                );

        return new CalificacionResponse(
                entidad.getId(),
                datosInscripcion,
                entidad.getCalificacion(),
                StringCustomUtils.localDateAString(entidad.getFechaIngreso())
        );
    }
}

