package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AulaComplementoTest {

    @Test
    void actualizar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        Aulas entidad = Aulas.crear("Aula 101",30);

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> entidad.actualizar("",30));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test
    void cambioEnDatos_nombreDistinto_retornaTrue() {
        // Arrange
        Aulas entidad = Aulas.crear("Aula 101",30);

        // Act
        boolean resultado = entidad.cambioEnDatos("Aula 202",30);

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    void cambioEnDatos_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        Aulas entidad = Aulas.crear("Aula 101",30);

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> entidad.cambioEnDatos("",30));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test void crear_datosValidos_retornaAula() {
        // Arrange
        String nombre="Aula 101";
        // Act
        Aulas e=Aulas.crear(nombre,30);
        // Assert
        assertThat(e.getNombre()).isEqualTo(nombre);
    }

    @Test void crear_capacidadNoPositiva_lanzaDatoInvalidoException() {
        // Arrange
        int capacidad=0;
        // Act
        var error=assertThrows(DatoInvalidoException.class,()->Aulas.crear("Aula 101",capacidad));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Aulas e=Aulas.crear("Aula 101",30);
        // Act
        e.actualizar("Aula 202",20);
        // Assert
        assertThat(e.getNombre()).isEqualTo("Aula 202");
    }
}
