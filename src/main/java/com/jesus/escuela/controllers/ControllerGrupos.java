package com.jesus.escuela.controllers;


import com.jesus.escuela.dtos.grupos.GrupoRequest;
import com.jesus.escuela.dtos.grupos.GrupoResponse;
import com.jesus.escuela.service.grupos.GrupoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "API Grupos", description = "Métodos para gestión de grupos")
public class ControllerGrupos extends CrudController<GrupoRequest, GrupoResponse, GrupoService> {

    public ControllerGrupos(GrupoService service) {
        super(service);
    }
}
