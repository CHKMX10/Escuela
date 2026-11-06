package com.jesus.escuela.service.alumno;


import com.jesus.escuela.dtos.alumnos.AlumnoRequest;
import com.jesus.escuela.dtos.alumnos.AlumnoResponse;
import com.jesus.escuela.entities.Alumnos;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.entities.Maestros;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.AlumnoMapper;
import com.jesus.escuela.repositories.AlumnoRepository;
import com.jesus.escuela.repositories.InscripcionRepository;
import com.jesus.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlumnoServiceImpl implements AlumnoService {
    private final AlumnoRepository alumnoRepository;

    private final InscripcionRepository inscripcionRepository;

    private final AlumnoMapper alumnoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {

        log.info("Listando a los alumnos");

        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {
        log.info("Registrando alumno");

        Alumnos alumnos = alumnoMapper.requestAEntidad(request,  generarEmail(request), generarMatricula(request));

        alumnoRepository.saveAndFlush(alumnos);

        log.info("Nuevo alumno {} registrado con matrícula {}",
                alumnos.obtenerNombreCompleto());
        return alumnoMapper.entidadAResponse(alumnos);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumnos alumnos = obtenerAlumno(id);

        if (alumnos.cambioEnDatos(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        )) {
            log.info("Actualizando alumno");
            alumnos.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    generarEmail(request),
                    generarMatricula(request)
            );

            alumnoRepository.saveAndFlush(alumnos);

            log.info("Alumno {} actualizado",
                    alumnos.obtenerNombreCompleto());
            }
        return alumnoMapper.entidadAResponse(alumnos);
    }

    @Override
    public void eliminar(Long id) {
    Alumnos alumnos = obtenerAlumno(id);
    log.info("Eliminando alumno...");

    if(inscripcionRepository.existsByAlumnoId(id))
        throw new EntidadRelacionadaException(
                "No se puede eliminar el alumno porque esta inscrito"
        );

    alumnoRepository.delete(alumnos);
    alumnoRepository.flush();

    log.info("Alumno {} eliminado", alumnos.obtenerNombreCompleto());
    }

    private String generarEmail(AlumnoRequest request){
        log.info("Generando Email.........");

        return alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }


    private String generarMatricula(AlumnoRequest request){
        log.info("Generando Matricula.........");

        return alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }



    private Alumnos obtenerAlumno(Long id){
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumnos.class);
    }
    private Alumnos obtenerAlumnoOException(Long id){

        log.info("Buscando Alumno con id: {}", id);

        return alumnoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Alumno no encontrado con id: "+id
                )
        );
    }
}
