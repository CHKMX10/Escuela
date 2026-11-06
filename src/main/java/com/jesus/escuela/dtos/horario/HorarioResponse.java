package com.jesus.escuela.dtos.horario;

import com.jesus.escuela.dtos.datos.DatosGrupoHorario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta detallada del horario")
public record HorarioResponse(
        @Schema(description = "ID único del horario", example = "1")
        Long id,

        @Schema(description = "Información del grupo asociado")
        DatosGrupoHorario grupo,

        @Schema(description = "Horario formateado", example = "Lunes 08:00 10:00")
        String horario

) {
}
