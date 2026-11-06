package com.jesus.escuela.controllers;

import com.jesus.escuela.dtos.alumnos.AlumnoRequest;
import com.jesus.escuela.dtos.alumnos.AlumnoResponse;
import com.jesus.escuela.service.alumno.AlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
@Tag(name = "API Alumnos", description = "Métodos para gestión de alumnos")
public class ControllerAlumno extends CrudController<AlumnoRequest , AlumnoResponse , AlumnoService> {
    public ControllerAlumno(AlumnoService service) {
        super(service);
    }
}
