package com.jesus.escuela.entities;

import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HorarioComplementoTest {



    private Grupos grupo() {
        return Grupos.builder().id(1L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
    }

    private Horarios horario() {
        return Horarios.builder().id(1L).grupo(grupo()).diaSem(DiaSem.LUNES).horaInicio("08:00").horaFin("10:00").build();
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

    @Test void crear_horaInicioMenorQueHoraFin_retornaHorario() {
        // Arrange
        Grupos g=grupo();
        // Act
        Horarios e=Horarios.crear(g,com.jesus.escuela.enums.DiaSem.LUNES,"08:00","10:00");
        // Assert
        assertThat(e.getHoraInicio()).isEqualTo("08:00");
    }

    @Test void actualizar_rangoValido_actualizaHorario() {
        // Arrange
        Horarios e=horario();
        // Act
        e.actualizar(e.getGrupo(),com.jesus.escuela.enums.DiaSem.LUNES,"09:00","11:00");
        // Assert
        assertThat(e.getHoraInicio()).isEqualTo("09:00");
    }
}
