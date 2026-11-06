package com.jesus.escuela.service.calificaciones;

import com.jesus.escuela.dtos.calificaciones.CalificacionResponse;
import com.jesus.escuela.dtos.calificaciones.CalificacionesRequest;
import com.jesus.escuela.entities.Calificaciones;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.CalificacionesMapper;
import com.jesus.escuela.repositories.CalificacionesRepository;
import com.jesus.escuela.repositories.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CalificacionServiceImpl implements CalificacionService{

    private final CalificacionesRepository calificacionRepository;

    private final InscripcionRepository inscripcionRepository;

    private final CalificacionesMapper calificacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listar() {
        log.info("Listando calificaciones");

        return calificacionRepository.findAll()
                .stream()
                .map(calificacionMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional
    public CalificacionResponse obtenerPorId(Long id) {
        log.info("Listando calificaciones con ID: {}", id);
        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionesRequest request) {
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        log.info("Registrando calificaciones");

        if (calificacionRepository.existsByInscripcionId(inscripcion.getId())) {
            throw new ConflictoException(
                    "La inscripción ya tiene una calificación"
            );
        }

        Calificaciones calificacion =
                calificacionMapper.requestAEntidad(request, inscripcion);

        calificacionRepository.saveAndFlush(calificacion);

        log.info("Calificaciones registradas");

        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionesRequest request, Long id) {
        Calificaciones calificacion = obtenerCalificacion(id);
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        log.info("Actualizando calificacion");

        boolean otraCalificacionExiste =
                calificacionRepository.existsByInscripcionIdAndIdNot(
                        inscripcion.getId(),
                        id
                );

        if (otraCalificacionExiste) {
            throw new ConflictoException(
                    "La inscripción ya tiene otra calificación"
            );
        }

        calificacion.actualizar(inscripcion, request.calificacion());

        Calificaciones actualizada =
                calificacionRepository.saveAndFlush(calificacion);
        log.info("Calificacion actualizada");

        return calificacionMapper.entidadAResponse(actualizada);    }

    @Override
    public void eliminar(Long id) {
        Calificaciones calificacion = obtenerCalificacion(id);
        log.info("Eliminando calificacion");

        calificacionRepository.delete(calificacion);
        log.info("Calificacion eliminada");

        calificacionRepository.flush();
    }

    private Calificaciones obtenerCalificacion(Long id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Calificación no encontrada con id: " + id
                        )
                );
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Inscripción no encontrada con id: " + id
                        )
                );
    }
}
