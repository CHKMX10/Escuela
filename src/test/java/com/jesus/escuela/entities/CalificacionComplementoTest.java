package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalificacionComplementoTest {



    private Inscripcion inscripcion() {
        return Inscripcion.builder().id(1L).alumno(alumno()).grupo(grupo()).build();
    }

    private Alumnos alumno() {
        return Alumnos.builder().id(1L).nombre("Carlos").apPaterno("Perez").apMaterno("Gomez").email("carlos@escuela.com").matricula("CAPEG02601").build();
    }

    private Grupos grupo() {
        return Grupos.builder().id(1L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
    }

    private Maestros maestro() {
        return Maestros.builder().id(1L).nombre("Laura").apPaterno("Perez").apMaterno("Gomez").email("laura@escuela.com").telefono("5512345678").build();
    }

    private Aulas aula() {
        return Aulas.builder().id(1L).nombre("Aula 101").capacidad(30).build();
    }

    private Cursos curso() {
        return Cursos.builder().id(1L).nombre("Matematicas I").descripcion("Fundamentos").creditos(6).build();
    }

    @Test void crear_valorValido_retornaCalificacion() {
        // Arrange
        Inscripcion i=inscripcion();
        // Act
        Calificaciones e=Calificaciones.crear(i,new java.math.BigDecimal("8.5"));
        // Assert
        assertThat(e.getCalificacion()).isEqualByComparingTo("8.5");
    }

    @Test void actualizar_valorValido_actualizaCalificacion() {
        // Arrange
        Calificaciones e=Calificaciones.crear(inscripcion(),new java.math.BigDecimal("8.5"));
        // Act
        e.actualizar(e.getInscripcion(),new java.math.BigDecimal("9"));
        // Assert
        assertThat(e.getCalificacion()).isEqualByComparingTo("9");
    }
}
