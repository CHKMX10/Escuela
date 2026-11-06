package com.jesus.escuela.controllers;

import com.jesus.escuela.entities.*;
import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.service.horario.HorarioService;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "server.port=0",
    "spring.datasource.url=jdbc:h2:mem:pruebasHorario;MODE=Oracle;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false",
    "logging.level.root=ERROR", "spring.main.banner-mode=off"
})
@AutoConfigureMockMvc
@Transactional
class HorarioControllerIT {
    private static final String RUTA = "/api/horarios";
    @Autowired MockMvc mvc;
    @Autowired EntityManager em;
    @MockitoSpyBean HorarioService service;
    Cursos curso;
    Maestros maestro;
    Aulas aula;
    Grupos grupo;


    @BeforeEach
    void prepararDatos() {
        curso = guardar(Cursos.crear("Matematicas I","Fundamentos",6));
        maestro = guardar(Maestros.crear("Laura","Perez","Gomez","laura@escuela.com","5512345678"));
        aula = guardar(Aulas.crear("Aula 101",30));
        grupo = guardar(Grupos.crear(curso,maestro,aula,"2026-01"));
    }








    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":999999,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}";
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







    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        Long id = 999999L;
        String json = requestValido();
        em.flush();
        em.clear();

        // Act
        ResultActions resultado = mvc.perform(put(RUTA+"/{id}",id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }


    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        Long id = 999999L;
        em.flush();
        em.clear();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA+"/{id}",id));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }





    private String requestValido() {
        return horarioJson(grupo,"08:00","10:00");
    }

    private Long prepararEntidad() {
        return guardarHorario(grupo,"08:00","10:00").getId();
    }

    private <T> T guardar(T entidad) {
        em.persist(entidad);
        em.flush();
        return entidad;
    }

    private Horarios guardarHorario(Grupos destino,String inicio,String fin) {
        return guardar(Horarios.crear(destino,DiaSem.LUNES,inicio,fin));
    }

    private String horarioJson(Grupos destino,String inicio,String fin) {
        return "{\"idGrupo\":%d,\"dia\":\"LUNES\",\"horaInicio\":\"%s\",\"horaFin\":\"%s\"}".formatted(destino.getId(),inicio,fin);
    }
}
