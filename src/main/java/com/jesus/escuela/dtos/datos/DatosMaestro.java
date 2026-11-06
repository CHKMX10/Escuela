package com.jesus.escuela.dtos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un Maestro")
public record DatosMaestro(

        @Schema(description = "Nombre del Maestro", example = "Juan Perez")
        String nombre,

        @Schema(description = "Email del maestro", example = "Email@maestro.com")
        String email,

        @Schema(description = "Telefono del maestro", example = "1234567890")
        String telefono
) {
}
