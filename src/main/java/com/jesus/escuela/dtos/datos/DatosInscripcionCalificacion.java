package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de la inscripción asociada a una calificación")
public record DatosInscripcionCalificacion(
        @Schema(description = "Datos del alumno", example = "Juan Pérez López")
        DatosAlumnoInscripcion alumno,

        @Schema(description = "Datos del curso", example = "Matemáticas I")
        DatosGrupoHorario grupo,

        @Schema(description = "Fecha de inscripcion", example = "12/12/2025")
        String fechaInscripcion
) {
}
