package com.jesus.escuela.dtos.inscripcion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos para inscribir un alumno en un grupo")
public record InscripcionRequest(

        @Schema(description = "ID del alumno", example = "1")
        @NotNull(message = "El ID del alumno es requerido")
        @Positive(message = "El ID del alumno debe ser positivo")
        Long idAlumno,

        @Schema(description = "ID del grupo", example = "2")
        @NotNull(message = "El ID del grupo es requerido")
        @Positive(message = "El ID del grupo debe ser positivo")
        Long idGrupo
) {
}
