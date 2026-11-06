package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CursoComplementoTest {

    @Test
    void actualizar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        Cursos entidad = Cursos.crear("Matematicas I","Fundamentos",6);

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> entidad.actualizar("","Fundamentos",6));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test
    void cambioEnDatos_nombreDistinto_retornaTrue() {
        // Arrange
        Cursos entidad = Cursos.crear("Matematicas I","Fundamentos",6);

        // Act
        boolean resultado = entidad.cambioEnDatos("Fisica","Fundamentos",6);

        // Assert
        assertThat(resultado).isTrue();
    }


    @Test void crear_datosValidos_retornaCurso() {
        // Arrange
        String nombre="Matematicas I";
        // Act
        Cursos e=Cursos.crear(nombre,"Fundamentos",6);
        // Assert
        assertThat(e.getNombre()).isEqualTo(nombre);
    }

    @Test void crear_creditosNoPositivos_lanzaDatoInvalidoException() {
        // Arrange
        int creditos=0;
        // Act
        var error=assertThrows(DatoInvalidoException.class,()->Cursos.crear("Matematicas I","Fundamentos",creditos));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Cursos e=Cursos.crear("Matematicas I","Fundamentos",6);
        // Act
        e.actualizar("Fisica I","Fundamentos",5);
        // Assert
        assertThat(e.getNombre()).isEqualTo("Fisica I");
    }
}
