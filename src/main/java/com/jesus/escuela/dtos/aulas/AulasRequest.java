package com.jesus.escuela.dtos.aulas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un Aula")
public record AulasRequest(

        @Schema(description = "Nombre del aula", example = "Aula 101")
        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @Schema(description = "Capacidad del Aula", example = "80")
        @NotNull(message = "La capacidad es requerido")
        @Positive(message = "La capacidad debe ser positiva")
        Integer capacidad
) {
}
