package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InscripcionComplementoTest {


    @Test void crear_alumnoYGrupoValidos_retornaInscripcionConFechaDeHoy() {
        // Arrange
        Alumnos a=Alumnos.crear("Carlos","Perez","Gomez"); Grupos g=new Grupos();
        // Act
        Inscripcion e=Inscripcion.crear(a,g);
        // Assert
        assertThat(e.getFechaInscripcion()).isEqualTo(java.time.LocalDate.now());
    }
}
