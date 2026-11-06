package com.jesus.escuela.service.aula;

import com.jesus.escuela.dtos.aulas.AulasRequest;
import com.jesus.escuela.dtos.aulas.AulasResponse;
import com.jesus.escuela.entities.Aulas;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.AulaMapper;
import com.jesus.escuela.repositories.AulaRepository;
import com.jesus.escuela.repositories.GrupoRepository;
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
public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;

    private final AulaMapper aulaMapper;
    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AulasResponse> listar() {

        log.info("Listando las Aulas");

        return aulaRepository.findAll().stream()
                .map(aulaMapper::entidadAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AulasResponse obtenerPorId(Long id) {
        log.info("Listando Aula por ID");
        Aulas aulas = aulaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Aula no encontrada con ID: "+ id
        ));
        return aulaMapper.entidadAResponse(aulas);
    }

    @Override
    @Transactional
    public AulasResponse registrar(AulasRequest request) {
        validarDatosUnicos(request);

        log.info("Registrando Aula");

        Aulas aulas = aulaMapper.requestAEntidad(request);

        aulaRepository.saveAndFlush(aulas);

        log.info("Nueva Aula registrada con ID: {}", aulas.getId());
        return aulaMapper.entidadAResponse(aulas);
    }

    @Override
    public AulasResponse actualizar(AulasRequest request, Long id) {

        Aulas aulas = obtenerAulas(id);

        validarCambiosUnicos(request, id);

        String nombreTrim = request.nombre()
                != null
                ?
                request.nombre().trim()
                : null;

        log.info("Actualizando el Aula con id: {} ", id);

        if(aulas.cambioEnDatos(nombreTrim , request.capacidad())){
            log.info("Actualizando el Aula");

            aulas.actualizar(
                    nombreTrim ,
                    request.capacidad()
            );

            aulaRepository.saveAndFlush(aulas);

            log.info("Aula {} actualizada" , aulas.getId());
        }
        return aulaMapper.entidadAResponse(aulas);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Aulas aulas = obtenerAulas(id);

        if(grupoRepository.existsByAulasId(id)){
            throw new EntidadRelacionadaException("No se puede eliminar un aula con grupos asociados");
        }
        log.info("Eliminando Aula con id: {}", id);

        aulaRepository.delete(aulas);
        aulaRepository.flush();

        log.info("Aula con id: {} eliminada", id);
    }



    private void validarDatosUnicos(AulasRequest request){
        log.info("Validando nombre único");

        if(aulaRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException(
                    "Ya existe un Aula con el nombre de: " + request.nombre()
            );
    }
    private void validarCambiosUnicos(AulasRequest request, Long id) {
        log.info("Validando nombre único");
        String nombreTrim = request.nombre().trim();

        boolean existe;
        if(id == null){
            existe = aulaRepository.existsByNombreIgnoreCase(nombreTrim);
        }else {
            existe = aulaRepository.existsByNombreIgnoreCaseAndIdNot(nombreTrim , id);
        }
        if (existe){
            throw new ConflictoException("Ya existe otra aula con el nombre: " + request.nombre());
        }
    }
    private Aulas obtenerAulas(Long id){
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aulas.class);
    }
}
