package com.jesus.escuela.service.grupos;

import com.jesus.escuela.dtos.grupos.GrupoRequest;
import com.jesus.escuela.dtos.grupos.GrupoResponse;
import com.jesus.escuela.entities.Aulas;
import com.jesus.escuela.entities.Cursos;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Maestros;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.mappers.GrupoMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GrupoServiceImpl implements GrupoService{

    private final GrupoRepository grupoRepository;
    private final CursoRepository cursoRepository;
    private final MaestrosRepository maestrosRepository;
    private final AulaRepository aulaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HorarioRepository horarioRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        log.info("Listando todos los maestros");
        return grupoRepository.findAll().stream()
                .map(grupoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        log.info("Listando maestro por ID: {}", id);

        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        log.info("Registrando Grupo");

        Cursos curso = obtenerCurso(request.cursoId());

        Maestros maestro = obtenerMaestro(request.maestroId());

        Aulas aula = obtenerAula(request.aulaId());

        validarGrupoUnico(request, null);

        Grupos grupo = grupoMapper.requestAEntidad(request, curso, maestro, aula);

        grupoRepository.saveAndFlush(grupo);

        log.info("Grupo registrado con ID: {}", grupo.getId());

        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        log.info("Actualizando Grupo con ID: {}", id);

        Grupos grupos = obtenerGrupo(id);

        Cursos cursos = obtenerCurso(request.cursoId());

        Maestros maestro = obtenerMaestro(request.maestroId());

        Aulas aula = obtenerAula(request.aulaId());

        validarGrupoUnico(request, id);

        grupos.actualizar(cursos, maestro, aula, request.periodo());

        grupoRepository.saveAndFlush(grupos);

        log.info("Grupo actualizado con ID: {}", id);

        return grupoMapper.entidadAResponse(grupos);
    }

    @Override
    public void eliminar(Long id) {
        Grupos grupo = obtenerGrupo(id);

        log.info("Eliminando grupo con id: {}", id);

        if (inscripcionRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo porque tiene inscripciones asociadas");

        if (horarioRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo porque tiene horarios asociados");

        grupoRepository.delete(grupo);
        grupoRepository.flush();

        log.info("Grupo con id {} eliminado", id);
    }

    private Grupos obtenerGrupo(Long id){
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupos.class);//ESTO SE VA A OCUPAR PARA TOD0, SE OBTIENE ABSTRACCION
    }

    private Cursos obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Cursos.class);
    }

    private Maestros obtenerMaestro(Long id) {
        return ServiceUtils.obtenerEntidadOException(maestrosRepository, id, Maestros.class);
    }

    private Aulas obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aulas.class);
    }

    private void validarGrupoUnico(GrupoRequest request, Long id) {
        log.info("Validando combinación única Curso + Maestro + Aula + Periodo");

        String periodo = request.periodo().trim();

        boolean existe = id == null
                ? grupoRepository.existsByCursosIdAndMaestroIdAndAulasIdAndPeriodo(
                request.cursoId(), request.maestroId(), request.aulaId(), periodo)
                : grupoRepository.existsByCursosIdAndMaestroIdAndAulasIdAndPeriodoAndIdNot(
                request.cursoId(), request.maestroId(), request.aulaId(), periodo, id);

        if (existe)
            throw new ConflictoException(
                    "Ya existe un grupo con el mismo curso, maestro, aula y periodo " + periodo);
    }
}
