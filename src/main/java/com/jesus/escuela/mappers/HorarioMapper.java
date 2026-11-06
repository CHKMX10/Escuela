package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.datos.DatosGrupoHorario;
import com.jesus.escuela.dtos.horario.HorarioRequest;
import com.jesus.escuela.dtos.horario.HorarioResponse;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Horarios;
import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.service.horario.HorarioService;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horarios> {

    @Override
    public Horarios requestAEntidad(HorarioRequest request) {
        return null;
    }

    public Horarios requestAEntidad(HorarioRequest request, Grupos grupo) {
        if (request == null) {
            return null;
        }

        DiaSem dia = DiaSem.obtenerDiaPorDescripcion(request.dia());

        return Horarios.crear(
                grupo,
                dia,
                request.horaInicio(),
                request.horaFin()
        );
    }

    @Override
    public HorarioResponse entidadAResponse(Horarios horario) {
        if (horario == null) {
            return null;
        }

        Grupos grupo = horario.getGrupo();

        DatosGrupoHorario datosGrupo = new DatosGrupoHorario(
                grupo.getCursos().getNombre(),
                grupo.getMaestro().obtenerNombreCompleto(),
                grupo.getAulas().getNombre(),
                grupo.getPeriodo()
        );

        String horarioFormateado =
                horario.getDiaSem().getDescripcion()
                        + " " + horario.getHoraInicio()
                        + " " + horario.getHoraFin();

        return new HorarioResponse(
                horario.getId(),
                datosGrupo,
                horarioFormateado
        );
    }
}
