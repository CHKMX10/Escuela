package com.jesus.escuela.mappers;

import com.jesus.escuela.dtos.aulas.AulasRequest;
import com.jesus.escuela.dtos.aulas.AulasResponse;
import com.jesus.escuela.entities.Aulas;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulasRequest , AulasResponse, Aulas> {

    @Override
    public Aulas requestAEntidad(AulasRequest request) {
        return request == null
                ? null
                : Aulas.crear(
                        request.nombre(),
                        request.capacidad()
        );
    }

    @Override
    public AulasResponse entidadAResponse(Aulas entidad) {
        return entidad == null
                ? null
                : new AulasResponse(
                        entidad.getId(),
                        entidad.getNombre(),
                        entidad.getCapacidad()
        );
    }

}
