package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos de una calificacion")
public record DatosCalificacion(
        @Schema(description = "Nombre del curso", example = "Matemáticas I")
        String curso,
        @Schema(description = "Periodo de la calificación", example = "2026-09")
        String periodo,
        @Schema(description = "Calificación del curso", example = "5.3")
        BigDecimal calificacion
) {
}
