package com.jesus.escuela.dtos.cursos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta con la información del Curso")
public record CursoResponse(

        @Schema(description = "ID del Curso", example = "1")
        Long id,

        @Schema(description = "Nombre del curso", example = "Matematicas 2")
        String nombre,

        @Schema(description = "Descripcion del curso", example = "Fundamentos matemáticos 2")
        String descripcion,

        @Schema(description = "Creditos del Aula", example = "6")
        Integer creditos
) {
}
