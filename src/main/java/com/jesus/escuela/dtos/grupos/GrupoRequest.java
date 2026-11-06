package com.jesus.escuela.dtos.grupos;

import com.jesus.escuela.entities.Horarios;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "DTO para la creación y actualización de Grupos")
public record GrupoRequest(
        @Schema(description = "ID del curso impartido", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El ID del curso es requerido")
        @Positive(message = "El ID del curso debe ser un número válido")
        Long cursoId,

        @Schema(description = "ID del maestro asignado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El ID del maestro es requerido")
        @Positive(message = "El ID del maestro debe ser un número válido")
        Long maestroId,

        @Schema(description = "ID del aula asignada", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El ID del aula es requerido")
        @Positive(message = "El ID del aula debe ser un número válido")
        Long aulaId,

        @Schema(description = "Periodo académico en formato yyyy-MM", example = "2026-01")
        @NotBlank(message = "El periodo es requerido")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$",
                message = "El periodo debe tener el formato yyyy-MM, por ejemplo 2026-01")
        String periodo
) {
}
