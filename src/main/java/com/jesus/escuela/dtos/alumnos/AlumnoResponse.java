package com.jesus.escuela.dtos.alumnos;

import com.jesus.escuela.dtos.datos.DatosCalificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Información del alumno")
public record AlumnoResponse(

        //----------AQUI SE TIENE QUE VER EL REQUERIMIENTO, SI SE PIDE NOMBRE, EMAIL, MATRICULA Y FECHA, AUNQUE ALUMNO NO LO LLEVE, SE PONE LO QUE DEBE TERMINAR COMO RESULTADO

        @Schema(description = "ID del alumno", example = "2")
        Long id,

        @Schema(description = "Nombre del alumno", example = "Gerardo")
        String nombre,

        @Schema(description = "Email del alumno", example = "ejemplo@alumno.com")
        String email,

        @Schema(description = "Matricula del alumno", example = "1234567890")
        String matricula,

        @Schema(description = "Fecha de ingreso del alumno", example = "Gerardo")
        String fechaIngreso,

        @Schema(description = "Calificaciones del alumno")
        List<DatosCalificacion> calificaciones,

        @Schema(description = "Promedio del alumno", example = "8.9")
        BigDecimal promedio
        ) {


}
