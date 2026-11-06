package com.jesus.escuela.controllers;


import com.jesus.escuela.dtos.calificaciones.CalificacionResponse;
import com.jesus.escuela.dtos.calificaciones.CalificacionesRequest;
import com.jesus.escuela.service.calificaciones.CalificacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "API Calificaciones", description = "Métodos para gestión de califiaciones")
public class ControllerCalificaciones extends CrudController<CalificacionesRequest, CalificacionResponse, CalificacionService> {
    public ControllerCalificaciones(CalificacionService service) {
        super(service);
    }
}
