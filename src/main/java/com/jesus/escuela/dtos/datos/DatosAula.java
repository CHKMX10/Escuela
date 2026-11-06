package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un Aula")
public record DatosAula(
        @Schema(description = "Nombre del Aula", example = "Juan Perez")
        String nombre,

        @Schema(description = "Capacidad del Aula", example = "30")
        Integer capacidad
) {
}
