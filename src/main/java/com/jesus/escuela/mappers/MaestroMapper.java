package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.datos.DatosCurso;
import com.jesus.escuela.dtos.maestros.MaestroRequest;
import com.jesus.escuela.dtos.maestros.MaestroResponse;
import com.jesus.escuela.entities.Grupos;
import com.jesus.escuela.entities.Maestros;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor//inyectar mapper
public class MaestroMapper implements CommonMapper<MaestroRequest, MaestroResponse, Maestros> {

    private final CursoMapper cursoMapper;

    @Override
    public Maestros requestAEntidad(MaestroRequest request) {

        return request == null
                ? null
                : Maestros.crear(
                        request.nombre(),
                        request.apPaterMaestro(),
                        request.apMaterMaestro(),
                        request.email(),
                        request.telefono());
    }

    @Override
    public MaestroResponse entidadAResponse(Maestros entidad) {
        return entidad == null
                ? null
                : new MaestroResponse(
                    entidad.getId(),
                entidad.obtenerNombreCompleto(),//este es obtenerNombreCompleto de maestro pero se ocupa el constructor automatico
                entidad.getEmail(),
                entidad.getTelefono(),
                null);
    }

    public List<DatosCurso> entidadADatosCurso(Maestros entidad){

        return entidad == null
                ? List.of()
                : entidad.getGrupos().stream()
                .map(Grupos::getCursos)
                .map(cursoMapper::entidadADatosCurso)
                .toList();
    }
}
