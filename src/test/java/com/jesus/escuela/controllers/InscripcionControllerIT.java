package com.jesus.escuela.controllers;

import com.jesus.escuela.entities.*;
import com.jesus.escuela.service.inscripciones.InscripcionesService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "server.port=0",
    "spring.datasource.url=jdbc:h2:mem:pruebasInscripcion;MODE=Oracle;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false",
    "logging.level.root=ERROR", "spring.main.banner-mode=off"
})
@AutoConfigureMockMvc
@Transactional
class InscripcionControllerIT {
    private static final String RUTA = "/api/inscripciones";
    @Autowired MockMvc mvc;
    @Autowired EntityManager em;
    @MockitoSpyBean InscripcionesService service;
    Cursos curso;
    Maestros maestro;
    Aulas aula;
    Grupos grupo;
    Alumnos alumno;


    @BeforeEach
    void prepararDatos() {
        curso = guardar(Cursos.crear("Matematicas I","Fundamentos",6));
        maestro = guardar(Maestros.crear("Laura","Perez","Gomez","laura@escuela.com","5512345678"));
        aula = guardar(Aulas.crear("Aula 101",30));
        grupo = guardar(Grupos.crear(curso,maestro,aula,"2026-01"));
        alumno = guardarAlumno();
    }



    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        String json = "{\"idAlumno\":999999,\"idGrupo\":%d}".formatted(grupo.getId());
        em.flush();
        em.clear();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }


    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        Long id = 999999L;
        em.flush();
        em.clear();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA+"/{id}",id));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }











    private String requestValido() {
        return "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(alumno.getId(),grupo.getId());
    }

    private Long prepararEntidad() {
        return guardar(Inscripcion.crear(alumno,grupo)).getId();
    }

    private <T> T guardar(T entidad) {
        em.persist(entidad);
        em.flush();
        return entidad;
    }

    private Alumnos guardarAlumno() {
        Alumnos nuevo = Alumnos.crear("Carlos","Perez","Gomez");
        nuevo.asignarDatosAcademicos("carlos@escuela.com","CAPEG02601");
        return guardar(nuevo);
    }
}
