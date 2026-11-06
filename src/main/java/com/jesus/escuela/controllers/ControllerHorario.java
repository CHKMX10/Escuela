package com.jesus.escuela.controllers;


import com.jesus.escuela.dtos.horario.HorarioRequest;
import com.jesus.escuela.dtos.horario.HorarioResponse;
import com.jesus.escuela.service.horario.HorarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horario")
@Tag(name = "API Horario", description = "Métodos para gestión de horario")
public class ControllerHorario extends CrudController<HorarioRequest, HorarioResponse, HorarioService>{
    public ControllerHorario(HorarioService service) {
        super(service);
    }
}
