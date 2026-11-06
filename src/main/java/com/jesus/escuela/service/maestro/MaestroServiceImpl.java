package com.jesus.escuela.service.maestro;

import com.jesus.escuela.dtos.maestros.MaestroRequest;
import com.jesus.escuela.dtos.maestros.MaestroResponse;
import com.jesus.escuela.entities.Maestros;
import com.jesus.escuela.exceptions.ConflictoException;
import com.jesus.escuela.exceptions.EntidadRelacionadaException;
import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.mappers.MaestroMapper;
import com.jesus.escuela.repositories.GrupoRepository;
import com.jesus.escuela.repositories.MaestrosRepository;
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
public class MaestroServiceImpl implements MaestroService {

    private final MaestrosRepository maestrosRepository;

    private final GrupoRepository grupoRepository;

    private final MaestroMapper maestroMapper;

    //----------------------METODOS DE MAESTROSERVICE---------------
    @Override

    public List<MaestroResponse> listar() {
        log.info("Listando todos los maestros");
        return maestrosRepository.findAll().stream()
                .map(maestroMapper::entidadAResponse)
                .toList();
    }

    @Override
    //@Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        log.info("Listando maestro por ID: {}", id);

        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {
        log.info("Registrando maestros");

        validarDatosUnicos(request);

        Maestros maestros = maestroMapper.requestAEntidad(request);

        maestrosRepository.saveAndFlush(maestros);

        log.info("Nuevo maestro {} registrado" , maestros.obtenerNombreCompleto());

        return maestroMapper.entidadAResponse(maestros);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        log.info("Actualizando una sucursal con id: {} ", id);

        Maestros maestros = obtenerMaestro(id);

        log.info("Actualizando maestro con id: {} ", id);

        validarCambiosUnicos(request, id);

        maestros.actualizar(
                request.nombre(),
                request.apPaterMaestro(),
                request.apMaterMaestro(),
                request.email(),
                request.telefono()
        );

        maestrosRepository.saveAndFlush(maestros);

        log.info("Maestro {} actualizado", maestros.obtenerNombreCompleto());

        return maestroMapper.entidadAResponse(maestros);
    }

    @Override
    public void eliminar(Long id) {
        Maestros maestros = obtenerMaestro(id);

        log.info("Eliminando maestro con id: {}", id);

        if(grupoRepository.existsByMaestroId(id))
            throw new EntidadRelacionadaException(
              "No se puede eliminar el maestro ya que tiene grupos asignados");
        maestrosRepository.delete(maestros);
        maestrosRepository.flush();

        log.info("Maestro con id {} eliminado", maestros.obtenerNombreCompleto());
    }

    //--------------METODOS POR CLASE IMPL----------------------------------

    private Maestros obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestrosRepository, id, Maestros.class);//ESTO SE VA A OCUPAR PARA TOD0, SE OBTIENE ABSTRACCION
    }

    public void validarDatosUnicos(MaestroRequest request){
        log.info("Validando Email único..");
        if(maestrosRepository.existsByEmailIgnoreCase(request.email().trim()))
            throw new ConflictoException("Ya existe un email registrado: "+ request.email());

        log.info("Validando Número único..");
        if (maestrosRepository.existsByTelefono(request.telefono().trim()))
            throw new ConflictoException("Ya existe el número de telefono: "+ request.telefono());
    }

    public void validarCambiosUnicos(MaestroRequest request, Long id){
        log.info("Validando cambio en Email único..");
        if(maestrosRepository.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id))
            throw new ConflictoException("Ya existe un email registrado: "+ request.email());

        log.info("Validando Número único..");
        if (maestrosRepository.existsByTelefonoAndIdNot(request.telefono().trim(), id))
            throw new ConflictoException("Ya existe el número de telefono: "+ request.telefono());
    }

    private Maestros obtenerMaestroOException(Long id){

        log.info("Buscando producto con id: {}", id);

        return maestrosRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: "+id
                )
        );
    }
}
