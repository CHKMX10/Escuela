package com.jesus.escuela.dtos.grupos;

import com.jesus.escuela.dtos.datos.DatosAula;
import com.jesus.escuela.dtos.datos.DatosCurso;
import com.jesus.escuela.dtos.datos.DatosMaestro;
import com.jesus.escuela.entities.Horarios;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de respuesta con la información del Grupo")
public record GrupoResponse(
        @Schema(description = "ID único del grupo", example = "1")
        Long id,

        @Schema(description = "Datos cursos")
        DatosCurso cursos,

        @Schema(description = "Datos del maestro")
        DatosMaestro maestros,

        @Schema(description = "Datos cursos")
        DatosAula aulas,

        @Schema(description = "Datos cursos")
        List<String> horarios,

        @Schema(description = "Periodo escolar", example = "2026-02")
        String periodo
) {
}
