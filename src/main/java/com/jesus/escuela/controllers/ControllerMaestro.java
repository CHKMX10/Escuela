package com.jesus.escuela.controllers;


import com.jesus.escuela.dtos.maestros.MaestroRequest;
import com.jesus.escuela.dtos.maestros.MaestroResponse;
import com.jesus.escuela.service.maestro.MaestroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maestros")
public class ControllerMaestro extends CrudController<MaestroRequest , MaestroResponse , MaestroService> {

    public ControllerMaestro(MaestroService service) {
        super(service);
    }
}
