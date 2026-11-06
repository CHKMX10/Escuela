package com.jesus.escuela.dtos.calificaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Datos para controlar una calificación")
public record CalificacionesRequest(
        @Schema(description = "ID de la inscripción", example = "1")
        @NotNull(message = "El ID de la inscripción es requerido")
        @Positive(message = "El ID de la inscripción debe ser positivo")
        Long idInscripcion,

        @Schema(description = "Nota del alumno", example = "8.5")
        @NotNull(message = "La calificación es requerida")
        BigDecimal calificacion
) {
}
