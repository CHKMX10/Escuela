package com.jesus.escuela.entities;

import com.jesus.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GrupoComplementoTest {



    private Maestros maestro() {
        return Maestros.builder().id(1L).nombre("Laura").apPaterno("Perez").apMaterno("Gomez").email("laura@escuela.com").telefono("5512345678").build();
    }

    private Aulas aula() {
        return Aulas.builder().id(1L).nombre("Aula 101").capacidad(30).build();
    }

    private Cursos curso() {
        return Cursos.builder().id(1L).nombre("Matematicas I").descripcion("Fundamentos").creditos(6).build();
    }

    private Grupos grupo() {
        return Grupos.builder().id(1L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
    }

    @Test void crear_datosValidos_retornaGrupo() {
        // Arrange
        Cursos c=Cursos.crear("Matematicas I","Fundamentos",6); Maestros m=Maestros.crear("Laura","Perez","Gomez","laura@escuela.com","5512345678"); Aulas a=Aulas.crear("Aula 101",30);
        // Act
        Grupos e=Grupos.crear(c,m,a,"2026-01");
        // Assert
        assertThat(e.getPeriodo()).isEqualTo("2026-01");
    }

    @Test void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Grupos e=grupo();
        // Act
        e.actualizar(e.getCursos(),e.getMaestro(),e.getAulas(),"2026-02");
        // Assert
        assertThat(e.getPeriodo()).isEqualTo("2026-02");
    }
}
