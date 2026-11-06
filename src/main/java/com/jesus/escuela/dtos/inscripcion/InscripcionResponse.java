package com.jesus.escuela.dtos.inscripcion;

import com.jesus.escuela.dtos.datos.DatosAlumnoInscripcion;
import com.jesus.escuela.dtos.datos.DatosGrupoHorario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InscripcionResponse(

        @Schema(description = "Identificador de la inscripción", example = "1")
        Long id,

        @Schema(description = "Datos del alumno inscrito")
        DatosAlumnoInscripcion alumno,

        @Schema(description = "Datos del grupo")
        DatosGrupoHorario grupo,

        @Schema(description = "Calificación del alumno; null si aún no tiene")
        BigDecimal calificacion,

        @Schema(description = "Fecha de inscripción", example = "11/02/2026")
        String fechaInscripcion
) {
}
