package com.jesus.escuela.service.horario;

import com.jesus.escuela.dtos.horario.HorarioRequest;
import com.jesus.escuela.dtos.horario.HorarioResponse;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Horarios;
import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.DatoInvalidoException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.HorarioMapper;
import com.jesus.escuela.repositories.GrupoRepository;
import com.jesus.escuela.repositories.HorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HorarioServiceImpl implements HorarioService{

    private final HorarioRepository horarioRepository;

    private final GrupoRepository grupoRepository;

    private final HorarioMapper horarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listar() {
        log.info("Listando todos los horarios");

        return horarioRepository
                .findAll()
                .stream()
                .map(horarioMapper::entidadAResponse).toList();
    }

    @Override
    public HorarioResponse obtenerPorId(Long id) {
        log.info("Listando Horario por ID");

        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        log.info("Registrando horario para el grupo: {}", request.idGrupo());

        Grupos grupo = obtenerGrupo(request.idGrupo());
        DiaSem diaSem = obtenerDia(request.dia());

        Horarios horario = crearEntidad(request, grupo);

        validarTraslapes(
                grupo.getId(),
                grupo.getAulas().getId(),
                grupo.getPeriodo(),
                diaSem,
                request.horaInicio(),
                request.horaFin(),
                null
        );

        return horarioMapper.entidadAResponse(
                horarioRepository.save(horario)
        );    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        log.info("Actualizando horario con id: {}", id);

        Horarios horarioExistente = obtenerHorario(id);
        Grupos grupo = obtenerGrupo(request.idGrupo());
        DiaSem diaSem = obtenerDia(request.dia());

        crearEntidad(request, grupo);

        validarTraslapes(
                grupo.getId(),
                grupo.getAulas().getId(),
                grupo.getPeriodo(),
                diaSem,
                request.horaInicio(),
                request.horaFin(),
                id
        );

        try {
            horarioExistente.actualizar(
                    grupo,
                    diaSem,
                    request.horaInicio(),
                    request.horaFin()
            );
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException(e.getMessage());
        }

        return horarioMapper.entidadAResponse(
                horarioRepository.save(horarioExistente)
        );    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando horario con id: {}", id);

        Horarios horario = obtenerHorario(id);
        horarioRepository.delete(horario);
    }
    private Horarios obtenerHorario(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado con id: " + id));
    }

    private Grupos obtenerGrupo(Long grupoId) {
        return grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo no encontrado con id: " + grupoId));
    }
    private DiaSem obtenerDia(String descripcion) {
        try {
            return DiaSem.obtenerDiaPorDescripcion(descripcion);
        } catch (RecursoNoEncontradoException e) {
            throw new DatoInvalidoException(
                    "El día indicado no es válido: " + descripcion
            );
        }
    }

    private Horarios crearEntidad(HorarioRequest request, Grupos grupo) {
        try {
            return horarioMapper.requestAEntidad(request, grupo);
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException(e.getMessage());
        }
    }

    private void validarTraslapes(Long grupoId, Long aulaId, String periodo,
                                  DiaSem diaSem, String horaInicio, String horaFin, Long idExcluir) {

        boolean traslapeGrupo =
                !horarioRepository.buscarTraslapesPorGrupo(
                        grupoId,
                        diaSem,
                        horaInicio,
                        horaFin,
                        idExcluir
                ).isEmpty();

        if (traslapeGrupo) {
            throw new ConflictoException(
                    "El horario se traslapa con otro horario del mismo grupo."
            );
        }

        boolean traslapeAula =
                !horarioRepository.buscarTraslapesPorAulaYPeriodo(
                        aulaId,
                        periodo,
                        grupoId,
                        diaSem,
                        horaInicio,
                        horaFin,
                        idExcluir
                ).isEmpty();

        if (traslapeAula) {
            throw new ConflictoException(
                    "El horario se traslapa con otro grupo en la misma aula "
                            + "para el periodo " + periodo + "."
            );
        }
    }
}
