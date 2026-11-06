package com.jesus.escuela.service.curso;

import com.jesus.escuela.dtos.aulas.AulasRequest;
import com.jesus.escuela.dtos.cursos.CursoRequest;
import com.jesus.escuela.dtos.cursos.CursoResponse;
import com.jesus.escuela.entities.Aulas;
import com.jesus.escuela.entities.Cursos;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.mappers.CursoMapper;
import com.jesus.escuela.repositories.CursoRepository;
import com.jesus.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {

        log.info("Listando las Aulas");

        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        log.info("Listando Cursos por ID");
        Cursos curso = obtenerCursos(id);
        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    @Transactional
    public CursoResponse registrar(CursoRequest request) {
        validarDatosUnicos(request);

        log.info("Registrando Curso");

        Cursos cursos = cursoMapper.requestAEntidad(request);

        cursoRepository.saveAndFlush(cursos);

        log.info("Nuevo curso registrado con ID: {}", cursos.getId());
        return cursoMapper.entidadAResponse(cursos);
    }

    @Override
    @Transactional
    public CursoResponse actualizar(CursoRequest request, Long id) {
        Cursos cursos = obtenerCursos(id);

        validarCambiosUnicos(request, id);
        log.info("Actualizando Curso por ID: {}", id);

        String nombreTrim = request.nombre().trim();

        String descTrim = request.descripcion()
                != null
                ? request.descripcion().trim()
                : null;

        if (cursos.cambioEnDatos(nombreTrim, descTrim, request.creditos())){
            log.info("Aplicando cambios en Curso");

            cursos.actualizar(nombreTrim, descTrim, request.creditos());

            cursoRepository.saveAndFlush(cursos);

            log.info("Curso {} actualizado correctamente", cursos.getId());
        }
        cursoRepository.saveAndFlush(cursos);
        log.info("Curso {} actualizado ", id);

        return cursoMapper.entidadAResponse(cursos);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cursos cursos = obtenerCursos(id);

        log.info("Eliminando Curso con ID: {}", id);

        cursoRepository.delete(cursos);

        cursoRepository.flush();

        log.info("Curso con ID: {} Eliminado", id);
    }

    private Cursos obtenerCursos(Long id){
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Cursos.class);
    }
    private void validarCambiosUnicos(CursoRequest request, Long id) {
        log.info("Validando curso único");
        String nombreTrim = request.nombre().trim();

        boolean existe;
        if(id == null){
            existe = cursoRepository.existsByNombreIgnoreCase(nombreTrim);
        }else {
            existe = cursoRepository.existsByNombreIgnoreCaseAndIdNot(nombreTrim , id);
        }
        if (existe){
            throw new ConflictoException("Ya existe otra aula con el nombre: " + request.nombre());
        }
    }
    private void validarDatosUnicos(CursoRequest request){
        log.info("Validando nombre único");

        if(cursoRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException(
                    "Ya existe un Curso con el nombre de: " + request.nombre()
            );
    }
}
