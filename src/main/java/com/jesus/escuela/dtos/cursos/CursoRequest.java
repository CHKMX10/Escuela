package com.jesus.escuela.dtos.cursos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos del curso necesarios para registrar o actualizar")
public record CursoRequest(

        @Schema(description = "Nombre del Curso", example = "Matemáticas 1")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1,max = 100,message = "El nombre debe contener entre 1 y 100 carateres")
        String nombre,

        @Schema(description = "Descripción el curso", example = "Fundamentos de Java")
        @NotBlank(message = "La descripción es requerido")
        @Size(min = 1,max = 200,message = "La descripcion debe contener entre 1 y 200 carateres")
        String descripcion,

        @Schema(description = "Los creditos del Curso", example = "5")
        @NotNull(message = "Los creditos son requeridos")
        @Positive
        Integer creditos
) {
}
