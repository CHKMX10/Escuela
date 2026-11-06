package com.jesus.escuela.entities;
import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlumnoTest {
    @Test void crear_datosValidos_retornaAlumnoConDatosSinEspacios() {
        // Arrange
        String nombre = " Carlos ";
        // Act
        Alumnos alumno = Alumnos.crear(nombre," Perez "," Gomez ");
        // Assert
        assertThat(alumno.getNombre()).isEqualTo("Carlos");
        assertThat(alumno.getApPaterno()).isEqualTo("Perez");
        assertThat(alumno.getApMaterno()).isEqualTo("Gomez");
        assertThat(alumno.getDateIngreso()).isEqualTo(LocalDate.now());
    }
    @Test void crear_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        String nombre = "";
        // Act
        var error = assertThrows(DatoInvalidoException.class,()->Alumnos.crear(nombre,"Perez","Gomez"));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test void cambioEnDatos_nombreDistinto_retornaTrue() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        boolean cambio = alumno.cambioEnDatos("Juanes","Perez","Gomez");
        // Assert
        assertThat(cambio).isTrue();
    }
    @Test void cambioEnDatos_mismosDatos_retornaFalse() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        boolean cambio = alumno.cambioEnDatos("Carlos","Perez","Gomez");
        // Assert
        assertThat(cambio).isFalse();
    }
    @Test void cambioEnDatos_apellidoPaternoVacio_lanzaDatoInvalidoException() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        var error = assertThrows(DatoInvalidoException.class,()->alumno.cambioEnDatos("Carlos","","Gomez"));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test void asignarDatosAcademicos_matriculaDeLongitudDistintaA10_lanzaDatoInvalidoException() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        var error = assertThrows(DatoInvalidoException.class,()->alumno.asignarDatosAcademicos("carlos@escuela.com","123"));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test void actualizar_datosValidos_actualizaDatosPersonalesYAcademicos() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        alumno.actualizar("Juanes","Perez","Gomez","juanes@escuela.com","JUPGO02601");
        // Assert
        assertThat(alumno.getNombre()).isEqualTo("Juanes");
        assertThat(alumno.getEmail()).isEqualTo("juanes@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("JUPGO02601");
    }
    @Test void actualizar_emailVacio_lanzaDatoInvalidoException() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        var error = assertThrows(DatoInvalidoException.class,()->alumno.actualizar("Carlos","Perez","Gomez","","CAPEG02601"));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test void calcularPromedio_conNulosYValores_ignoraNulos() {
        // Arrange
        Alumnos alumno = alumno();
        alumno.getInscripciones().add(Inscripcion.builder().calificaciones(Calificaciones.builder().calificacion(new BigDecimal("8")).build()).build());
        alumno.getInscripciones().add(Inscripcion.builder().build());
        // Act
        BigDecimal promedio = alumno.calcularPromedio();
        // Assert
        assertThat(promedio).isEqualByComparingTo("8.00");
    }
    @Test void calcularPromedio_resultadoConDecimales_redondeaADosDecimalesHalfUp() {
        // Arrange
        Alumnos alumno = alumno();
        alumno.getInscripciones().add(Inscripcion.builder().calificaciones(Calificaciones.builder().calificacion(new BigDecimal("8.335")).build()).build());
        // Act
        BigDecimal promedio = alumno.calcularPromedio();
        // Assert
        assertThat(promedio).isEqualByComparingTo("8.34");
    }
    @Test void calcularPromedio_sinCalificaciones_retornaCero() {
        // Arrange
        Alumnos alumno = alumno();
        // Act
        BigDecimal promedio = alumno.calcularPromedio();
        // Assert
        assertThat(promedio).isEqualByComparingTo("0.00");
    }
    private Alumnos alumno() {return Alumnos.crear("Carlos","Perez","Gomez");}
}
