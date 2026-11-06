package com.jesus.escuela.dtos.datos;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos resumidos del grupo para el detalle del horario")
public record DatosGrupoHorario(
        @Schema(description = "Nombre del curso", example = "Matematicas 2")
        String curso,

        @Schema(description = "Nombre del maestro", example = "Jose Antonio Arco")
        String maestro,

        @Schema(description = "Nombre del aula", example = "Aula 5")
        String aula,

        @Schema(description = "Periodo", example = "5")
        String periodo

        ) {
}
