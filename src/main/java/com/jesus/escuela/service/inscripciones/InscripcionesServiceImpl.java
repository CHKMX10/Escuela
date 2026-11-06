package com.jesus.escuela.service.inscripciones;

import com.jesus.escuela.dtos.inscripcion.InscripcionRequest;
import com.jesus.escuela.dtos.inscripcion.InscripcionResponse;
import com.jesus.escuela.entities.Alumnos;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.InscripcionMapper;
import com.jesus.escuela.repositories.AlumnoRepository;
import com.jesus.escuela.repositories.GrupoRepository;
import com.jesus.escuela.repositories.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InscripcionesServiceImpl implements InscripcionesService{

    private final InscripcionRepository inscripcionRepository;

    private final AlumnoRepository alumnoRepository;

    private final GrupoRepository grupoRepository;

    private final InscripcionMapper inscripcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar() {
        log.info("Listando inscripciones");

        return inscripcionRepository.findAll()
                .stream()
                .map(inscripcionMapper::entidadAResponse)
                .toList();    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse obtenerPorId(Long id) {
        log.info("Listando inscripcion por ID");
        return inscripcionMapper.entidadAResponse(obtenerInscripcion(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {
        Alumnos alumno = obtenerAlumno(request.idAlumno());
        Grupos grupo = obtenerGrupo(request.idGrupo());
        log.info("Registrando inscripciones");

        validarInscripcionUnica(alumno.getId(), grupo.getId(), null);

        Inscripcion inscripcion =
                inscripcionMapper.requestAEntidad(request, alumno, grupo);

        Inscripcion guardada =
                inscripcionRepository.saveAndFlush(inscripcion);

        log.info("Inscripcion registrada");

        return inscripcionMapper.entidadAResponse(guardada);    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);
        Alumnos alumno = obtenerAlumno(request.idAlumno());
        Grupos grupo = obtenerGrupo(request.idGrupo());

        log.info("Actualizando inscripcion");

        validarInscripcionUnica(alumno.getId(), grupo.getId(), id);

        inscripcion.actualizar(alumno, grupo);

        Inscripcion actualizada =
                inscripcionRepository.saveAndFlush(inscripcion);
        log.info("Inscripcion actualizada");

        return inscripcionMapper.entidadAResponse(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);
        log.info("Eliminanado inscripcion");

        if (inscripcion.getCalificaciones() != null) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar la inscripción porque tiene una calificación asociada"
            );
        }

        inscripcionRepository.delete(inscripcion);
        inscripcionRepository.flush();
        log.info("Inscripcion eliminada");

    }
    private void validarInscripcionUnica(
            Long alumnoId,
            Long grupoId,
            Long idInscripcion) {

        boolean existe = idInscripcion == null
                ? inscripcionRepository.existsByAlumnoIdAndGrupoId(
                alumnoId, grupoId
        )
                : inscripcionRepository.existsByAlumnoAndGrupoIdAndIdNot(
                alumnoId, grupoId, idInscripcion
        );

        if (existe) {
            throw new ConflictoException(
                    "El alumno ya está inscrito en ese grupo"
            );
        }
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Inscripción no encontrada con id: " + id
                        )
                );
    }

    private Alumnos obtenerAlumno(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Alumno no encontrado con id: " + id
                        )
                );
    }

    private Grupos obtenerGrupo(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Grupo no encontrado con id: " + id
                        )
                );
    }
}
