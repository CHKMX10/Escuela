package com.jesus.escuela.service.maestro;

import com.jesus.escuela.dtos.maestros.MaestroRequest;
import com.jesus.escuela.dtos.maestros.MaestroResponse;
import com.jesus.escuela.entities.Cursos;
import com.jesus.escuela.mappers.MaestroMapper;
import com.jesus.escuela.repositories.GrupoRepository;
import com.jesus.escuela.repositories.MaestrosRepository;
import com.jesus.escuela.service.CrudService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


public interface MaestroService extends CrudService<MaestroRequest, MaestroResponse> {


}
