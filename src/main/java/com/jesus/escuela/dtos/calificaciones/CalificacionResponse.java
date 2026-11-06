package com.jesus.escuela.dtos.calificaciones;

import com.jesus.escuela.dtos.datos.DatosInscripcionCalificacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos de calificaciones")
public record CalificacionResponse(
        @Schema(description = "Id del curso", example = "1")
        Long id,

        @Schema(description = "Inscripción")
        DatosInscripcionCalificacion inscripcion,

        @Schema(description = "Calificacion del curso")
        BigDecimal calificacion,

        @Schema(description = "Fecha de registro al curso", example = "11/02/2026")
        String fechaRegistro
) {
}
