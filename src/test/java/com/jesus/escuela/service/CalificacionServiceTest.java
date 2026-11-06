package com.jesus.escuela.service;

import com.jesus.escuela.dtos.calificaciones.*;
import com.jesus.escuela.entities.*;
import com.jesus.escuela.exceptions.*;
import com.jesus.escuela.mappers.CalificacionesMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.service.calificaciones.CalificacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {
    @Mock CalificacionesRepository calificacionRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock CalificacionesMapper calificacionesMapper;
    @InjectMocks CalificacionServiceImpl service;


    @Test
    void registrar_datosValidos_registraCalificacion() {
        // Arrange
        CalificacionesRequest request = new CalificacionesRequest(1L,new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion()));
        CalificacionResponse esperado = mock(CalificacionResponse.class);
        when(calificacionesMapper.entidadAResponse(any(Calificaciones.class))).thenReturn(esperado);

        when(calificacionRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        when(calificacionesMapper.requestAEntidad(any(),any())).thenAnswer(inv -> Calificaciones.crear(inv.getArgument(1),new BigDecimal("8.5")));

        // Act
        var resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Calificaciones> captura = org.mockito.ArgumentCaptor.forClass(Calificaciones.class);
        verify(calificacionRepository).saveAndFlush(captura.capture());
        Calificaciones guardado = captura.getValue();
        assertThat(guardado.getInscripcion().getId()).isEqualTo(request.idInscripcion());
        assertThat(guardado.getCalificacion()).isEqualByComparingTo(request.calificacion());
        verify(calificacionesMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_inscripcionYaCalificada_lanzaConflictoException() {
        // Arrange
        CalificacionesRequest request = new CalificacionesRequest(1L,new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion()));
        when(calificacionRepository.existsByInscripcionId(1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository,never()).saveAndFlush(any());
    }

    @Test
    void registrar_inscripcionInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        CalificacionesRequest request = new CalificacionesRequest(1L,new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository,never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_calificacionExistente_retornaCalificacion() {
        // Arrange
        Calificaciones entidad = calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        CalificacionResponse esperado = mock(CalificacionResponse.class);
        when(calificacionesMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange


        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.obtenerPorId(999L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository,never()).saveAndFlush(any());
    }

    @Test
    void listar_conCalificaciones_retornaLista() {
        // Arrange
        Calificaciones entidad = calificacion();
        when(calificacionRepository.findAll()).thenReturn(List.of(entidad));
        CalificacionResponse esperado = mock(CalificacionResponse.class);
        when(calificacionesMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("Error de repositorio");
        when(calificacionRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class,service::listar);

        // Assert
        assertThat(error).isSameAs(causa);
        verify(calificacionRepository,never()).saveAndFlush(any());
    }

    @Test
    void actualizar_valorValido_actualizaCalificacion() {
        // Arrange
        Calificaciones entidad = calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        CalificacionesRequest request = new CalificacionesRequest(1L,new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion()));
        CalificacionResponse esperado = mock(CalificacionResponse.class);
        when(calificacionesMapper.entidadAResponse(entidad)).thenReturn(esperado);

        when(calificacionRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.actualizar(request,1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(calificacionRepository).saveAndFlush(entidad);
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        CalificacionesRequest request = new CalificacionesRequest(1L,new BigDecimal("8.5"));

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository,never()).saveAndFlush(any());
    }

    @Test
    void eliminar_calificacionExistente_eliminaCalificacion() {
        // Arrange
        Calificaciones entidad = calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(calificacionRepository).delete(entidad);
        verify(calificacionRepository).flush();
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange


        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.eliminar(999L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository,never()).saveAndFlush(any());
        verify(calificacionRepository,never()).delete(any());
    }

    private Inscripcion inscripcion() {
        return Inscripcion.builder().id(1L).alumno(alumno()).grupo(grupo()).build();
    }

    private Calificaciones calificacion() {
        return Calificaciones.builder().id(1L).inscripcion(inscripcion()).calificacion(new BigDecimal("8.5")).fechaIngreso(LocalDate.now()).build();
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
}

