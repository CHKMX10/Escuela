package com.jesus.escuela.dtos.horario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para el control de Horarios")
public record HorarioRequest(
        @Schema(description = "ID del grupo asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El ID del grupo es requerido")
        @Positive(message = "El ID del grupo debe ser un número válido")
        Long idGrupo,

        @Schema(description = "Día de la semana", example = "Lunes", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El día es requerido")
        String dia,

        @Schema(description = "Hora de inicio en formato HH:mm", example = "08:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La hora de inicio es requerida")
        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "El formato de hora debe ser HH:mm")
        String horaInicio,

        @Schema(description = "Hora de fin en formato HH:mm", example = "10:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La hora de fin es requerida")
        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "El formato de hora debe ser HH:mm")
        String horaFin
) {
}
