package com.jesus.escuela.dtos.alumnos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un alumno")
public record AlumnoRequest(
        //--------------------------no se ponen los valores generados automaticamente en la db
        // -------------------------como id, matricula etc-----------------------

        @Schema(description = "Nombre del alumno", example = "Gerardo")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1,max = 50,message = "El nombre debe contener entre 1 y 50 carateres")
        String nombre,

        @Schema(description = "Apellido paterno del alumno", example = "Pérez")
        @NotBlank(message = "El Apellido paterno es requerido")
        @Size(min = 1,max = 50,message = "El Apellido paterno debe contener entre 1 y 50 carateres")
        String apellidoPaterno,

        @Schema(description = "Apellido materno del alumno", example = "Zavala")
        @NotBlank(message = "El Apellido materno es requerido")
        @Size(min = 1,max = 50,message = "El Apellido materno debe contener entre 1 y 50 carateres")
        String apellidoMaterno
) {
}
