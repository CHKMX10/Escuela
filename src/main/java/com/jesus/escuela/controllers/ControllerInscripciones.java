package com.jesus.escuela.controllers;

import com.jesus.escuela.dtos.inscripcion.InscripcionRequest;
import com.jesus.escuela.dtos.inscripcion.InscripcionResponse;
import com.jesus.escuela.entities.Inscripcion;
import com.jesus.escuela.service.inscripciones.InscripcionesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/inscripcion")
    @Tag(name = "API Inscripciones", description = "Métodos para gestión de inscripciones")
public class ControllerInscripciones extends CrudController<InscripcionRequest, InscripcionResponse, InscripcionesService>{
    public ControllerInscripciones(InscripcionesService service) {
        super(service);
    }
}
