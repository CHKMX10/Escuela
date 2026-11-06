package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del alumno y una inscripción")
public record DatosAlumnoInscripcion(

        @Schema(example = "Juan Pérez López")
        String nombre,

        @Schema(example = "A2025001")
        String matricula,

        @Schema(example = "juan.perez@alumnos.com")
        String email,

        @Schema(description = "Fecha de ingreso del alumno", example = "10/01/2025")
        String fechaIngreso
) {
}
