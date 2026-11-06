package com.jesus.escuela.dtos.aulas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Información del Aula")
public record AulasResponse(

        @Schema(description = "ID del Aula", example = "1")
        Long id,

        @Schema(description = "Nombre del aula", example = "Aula 101")
        String nombre,

        @Schema(description = "Capacidad del Aula", example = "80")
        Integer capacidad
) {
}
