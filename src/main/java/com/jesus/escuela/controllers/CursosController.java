package com.jesus.escuela.controllers;

import com.jesus.escuela.dtos.cursos.CursoRequest;
import com.jesus.escuela.dtos.cursos.CursoResponse;
import com.jesus.escuela.service.curso.CursoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "API Cursos", description = "Métodos para gestión de Cursos")
public class CursosController extends CrudController<CursoRequest, CursoResponse, CursoService>{

    public CursosController(CursoService service) {
        super(service);
    }
}
