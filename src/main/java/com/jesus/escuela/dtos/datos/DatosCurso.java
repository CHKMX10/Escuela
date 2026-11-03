package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de un curso")
public record DatosCurso(

        @Schema(description = "Nombre del curso", example = "Matematicas 2")
        String nombre,

        @Schema(description = "Descripcion del curso", example = "Curso de cálculo integral")
        String descripcion,

        @Schema(description = "Créditos del curso", example = "5")
        Integer creditos
) {

}
