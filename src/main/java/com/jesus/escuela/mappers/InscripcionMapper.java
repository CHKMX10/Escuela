package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.datos.DatosAlumnoInscripcion;
import com.jesus.escuela.dtos.datos.DatosGrupoHorario;
import com.jesus.escuela.dtos.inscripcion.InscripcionRequest;
import com.jesus.escuela.dtos.inscripcion.InscripcionResponse;
import com.jesus.escuela.entities.Alumnos;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InscripcionMapper implements CommonMapper<InscripcionRequest, InscripcionResponse, Inscripcion> {

    public Inscripcion requestAEntidad(
            InscripcionRequest request,
            Alumnos alumno,
            Grupos grupo) {

        if (request == null || alumno == null || grupo == null) {
            return null;
        }

        return Inscripcion.crear(alumno, grupo);
    }

    @Override
    public Inscripcion requestAEntidad(InscripcionRequest request) {
        return null;
    }

    @Override
    public InscripcionResponse entidadAResponse(Inscripcion entidad) {
        if (entidad == null) {
            return null;
        }

        Alumnos alumno = entidad.getAlumno();
        Grupos grupo = entidad.getGrupo();

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

        var calificacion = entidad.getCalificaciones() == null
                ? null
                : entidad.getCalificaciones().getCalificacion();

        return new InscripcionResponse(
                entidad.getId(),
                datosAlumno,
                datosGrupo,
                calificacion,
                StringCustomUtils.localDateAString(
                        entidad.getFechaInscripcion()
                )
        );
    }
}
