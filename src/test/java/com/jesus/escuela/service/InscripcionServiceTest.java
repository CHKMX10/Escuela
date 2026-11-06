package com.jesus.escuela.service;

import com.jesus.escuela.dtos.inscripcion.*;
import com.jesus.escuela.entities.*;
import com.jesus.escuela.exceptions.*;
import com.jesus.escuela.mappers.InscripcionMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.service.inscripciones.InscripcionesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {
    @Mock AlumnoRepository alumnoRepository;
    @Mock CalificacionesRepository calificacionRepository;
    @Mock GrupoRepository grupoRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock InscripcionMapper inscripcionMapper;
    @InjectMocks InscripcionesServiceImpl service;


    @Test
    void registrar_datosValidos_registraInscripcion() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(1L,1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo()));
        InscripcionResponse esperado = mock(InscripcionResponse.class);
        when(inscripcionMapper.entidadAResponse(any(Inscripcion.class))).thenReturn(esperado);

        when(inscripcionRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        when(inscripcionMapper.requestAEntidad(any(),any(),any())).thenAnswer(inv -> Inscripcion.crear(inv.getArgument(1),inv.getArgument(2)));

        // Act
        var resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Inscripcion> captura = org.mockito.ArgumentCaptor.forClass(Inscripcion.class);
        verify(inscripcionRepository).saveAndFlush(captura.capture());
        Inscripcion guardado = captura.getValue();
        assertThat(guardado.getAlumno().getId()).isEqualTo(request.idAlumno());
        assertThat(guardado.getGrupo().getId()).isEqualTo(request.idGrupo());
        assertThat(guardado.getCalificaciones()).isNull();
        verify(inscripcionMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_alumnoYGrupoDuplicados_lanzaConflictoException() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(1L,1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo()));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoId(1L,1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository,never()).saveAndFlush(any());
    }

    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(1L,1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository,never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_inscripcionExistente_retornaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        InscripcionResponse esperado = mock(InscripcionResponse.class);
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

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
        verify(inscripcionRepository,never()).saveAndFlush(any());
    }

    @Test
    void listar_conInscripciones_retornaLista() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findAll()).thenReturn(List.of(entidad));
        InscripcionResponse esperado = mock(InscripcionResponse.class);
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("Error de repositorio");
        when(inscripcionRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class,service::listar);

        // Assert
        assertThat(error).isSameAs(causa);
        verify(inscripcionRepository,never()).saveAndFlush(any());
    }

    @Test
    void actualizar_otroGrupoValido_actualizaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        InscripcionRequest request = new InscripcionRequest(1L,2L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno()));
        Grupos destino = grupo();
        destino = Grupos.builder().id(2L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-02").build();
        when(grupoRepository.findById(2L)).thenReturn(Optional.of(destino));
        InscripcionResponse esperado = mock(InscripcionResponse.class);
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        when(inscripcionRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.actualizar(request,1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(inscripcionRepository).saveAndFlush(entidad);
    }

    @Test
    void actualizar_combinacionYaExistente_lanzaConflictoException() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        InscripcionRequest request = new InscripcionRequest(1L,2L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno()));
        Grupos destino = grupo();
        destino = Grupos.builder().id(2L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-02").build();
        when(grupoRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(inscripcionRepository.existsByAlumnoAndGrupoIdAndIdNot(1L,2L,1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository,never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinCalificacion_eliminaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(inscripcionRepository).delete(entidad);
        verify(inscripcionRepository).flush();
    }

    @Test
    void eliminar_conCalificacion_lanzaEntidadRelacionadaException() {
        // Arrange
        Inscripcion entidad = Inscripcion.builder().id(1L).alumno(alumno()).grupo(grupo()).calificaciones(Calificaciones.builder().calificacion(new java.math.BigDecimal("8")).build()).build();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class,() -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository,never()).saveAndFlush(any());
        verify(inscripcionRepository,never()).delete(any());
    }

    private Alumnos alumno() {
        return Alumnos.builder().id(1L).nombre("Carlos").apPaterno("Perez").apMaterno("Gomez").email("carlos@escuela.com").matricula("CAPEG02601").build();
    }

    private Grupos grupo() {
        return Grupos.builder().id(1L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
    }

    private Inscripcion inscripcion() {
        return Inscripcion.builder().id(1L).alumno(alumno()).grupo(grupo()).build();
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

