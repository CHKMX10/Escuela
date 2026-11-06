package com.jesus.escuela.controllers;


import com.jesus.escuela.dtos.aulas.AulasRequest;
import com.jesus.escuela.dtos.aulas.AulasResponse;
import com.jesus.escuela.service.aula.AulaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aula")
@Tag(name = "API Aula", description = "Métodos para gestión de Aulas")
public class ControllerAula extends CrudController<AulasRequest, AulasResponse, AulaService> {

    public ControllerAula(AulaService service) {
        super(service);
    }
}
