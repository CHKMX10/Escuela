package com.jesus.escuela.dtos.maestros;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un maestro")
public record MaestroRequest(

    @Schema(description = "Nombre del maestro", example = "Geremai")
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    String nombre,

    @Schema(description = "Apellido paterno del maestro", example = "Guzman")
    @NotBlank(message = "El apellido paterno es requerido")
    @Size(min = 1, max = 50, message = "El apellido debe tener entre 1 y 50 caracteres")
    String apPaterMaestro,

    @Schema(description = "Apellido materno del maestro", example = "Lopez")
    @NotBlank(message = "El apellido materno es requerido")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    String apMaterMaestro,

    @Schema(description = "Email del maestro", example = "test@test.com")
    @NotBlank(message = "El Email es requerido")
    @Size(min = 1, max = 50, message = "El Email debe tener entre 8 y 100 caracteres")
    @Email(message = "El email debe tener un formato valido (ejemplo@dominio.com)")
    String email,

    @Schema(description = "Numero de telefono", example = "1234567890")
    @NotBlank(message = "El numero es requerido")
    @Size(min = 10, max = 10, message = "El nombre debe tener entre 10 y 10 caracteres")
    String telefono

){}
