package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de la inscripción asociada a una calificación")
public record DatosInscripcionCalificacion(
        @Schema(description = "Nombre del alumno", example = "Juan Pérez López")
        String alumno,

        @Schema(description = "Nombre del curso", example = "Matemáticas I")
        String curso,

        @Schema(description = "Periodo del curso", example = "2025-01")
        String periodo
) {
}
