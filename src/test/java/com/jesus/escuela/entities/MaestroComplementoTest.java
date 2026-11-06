package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaestroComplementoTest {

    @Test
    void crear_datosValidos_retornaMaestroConDatosSinEspacios() {
        // Arrange
        String nombre = " Laura ";

        // Act
        Maestros maestro = Maestros.crear(nombre," Perez "," Gomez "," LAURA@ESCUELA.COM ","5512345678");

        // Assert
        assertThat(maestro.getNombre()).isEqualTo("Laura");
        assertThat(maestro.getApPaterno()).isEqualTo("Perez");
        assertThat(maestro.getApMaterno()).isEqualTo("Gomez");
        assertThat(maestro.getEmail()).isEqualTo("laura@escuela.com");
    }

    @Test
    void crear_emailVacio_lanzaDatoInvalidoException() {
        // Arrange
        String email = "";

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> Maestros.crear("Laura","Perez","Gomez",email,"5512345678"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test
    void actualizar_telefonoInvalido_lanzaDatoInvalidoException() {
        // Arrange
        Maestros maestro = Maestros.crear("Laura","Perez","Gomez","laura@escuela.com","5512345678");

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> maestro.actualizar("Laura","Perez","Gomez","laura@escuela.com","123"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Maestros e=Maestros.crear("Laura","Perez","Gomez","laura@escuela.com","5512345678");
        // Act
        e.actualizar("Anabel","Perez","Gomez","anabel@escuela.com","5599999999");
        // Assert
        assertThat(e.getNombre()).isEqualTo("Anabel");
    }
}
