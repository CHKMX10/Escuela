package com.jesus.escuela.dtos.maestros;

import com.jesus.escuela.dtos.datos.DatosCurso;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Datos necesarios para registrar o actualizar un maestro")
public record MaestroResponse (

        @Schema(description = "Id del maestro", example = "1")
        Long id,

        @Schema(description = "Nombre del maestro", example = "Juan")
        String nombre,

        @Schema(description = "Email del maestro", example = "juan@maes.com")
        String email,

        @Schema(description = "Telefono del maestro", example = "1234567890")
        String telefono,

        @Schema(description = "Datos del curso")
        List<DatosCurso> cursos

){
}
