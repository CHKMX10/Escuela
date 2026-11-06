package com.jesus.escuela.service;
import com.jesus.escuela.dtos.alumnos.*;
import com.jesus.escuela.entities.*;
import com.jesus.escuela.exceptions.*;
import com.jesus.escuela.mappers.AlumnoMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.service.alumno.AlumnoServiceImpl;
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
class AlumnoServiceTest {
    @Mock AlumnoRepository alumnoRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock AlumnoMapper alumnoMapper;
    @InjectMocks AlumnoServiceImpl service;
    private Alumnos alumno() {return Alumnos.builder().id(1L).nombre("Carlos").apPaterno("Perez").apMaterno("Gomez").email("carlos@escuela.com").matricula("CAPEG02601").build();}
    private AlumnoRequest request() {return new AlumnoRequest("Carlos","Perez","Gomez");}
    @Test void registrar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        AlumnoRequest request = new AlumnoRequest("","Perez","Gomez");
        when(alumnoMapper.requestAEntidad(eq(request),any(),any())).thenAnswer(inv -> Alumnos.crear(request.nombre(),request.apellidoPaterno(),request.apellidoMaterno()));
        // Act
        var error = assertThrows(DatoInvalidoException.class,()->service.registrar(request));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository,never()).saveAndFlush(any());
    }
    @Test void obtenerPorId_alumnoExistente_retornaAlumnoConPromedio() {
        // Arrange
        Alumnos entidad=alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        AlumnoResponse esperado=mock(AlumnoResponse.class);
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);
        // Act
        AlumnoResponse resultado=service.obtenerPorId(1L);
        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());
        // Act
        var error=assertThrows(RecursoNoEncontradoException.class,()->service.obtenerPorId(999L));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository,never()).saveAndFlush(any());
    }
    @Test void listar_conAlumnos_retornaLista() {
        // Arrange
        Alumnos entidad=alumno();
        when(alumnoRepository.findAll()).thenReturn(List.of(entidad));
        AlumnoResponse esperado=mock(AlumnoResponse.class);
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);
        // Act
        List<AlumnoResponse> resultado=service.listar();
        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa=new IllegalStateException("BD");
        when(alumnoRepository.findAll()).thenThrow(causa);
        // Act
        var error=assertThrows(IllegalStateException.class,service::listar);
        // Assert
        assertThat(error).isSameAs(causa);
        verify(alumnoRepository,never()).saveAndFlush(any());
    }
    @Test void actualizar_cambiaNombre_regeneraMatriculaYEmail() {
        // Arrange
        Alumnos entidad=alumno();
        AlumnoRequest request=new AlumnoRequest("Juanes","Perez","Gomez");
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoRepository.generarEmail(any(),any(),any())).thenReturn("juanes@escuela.com");
        when(alumnoRepository.generarMatricula(any(),any(),any())).thenReturn("JUPGO02601");
        // Act
        service.actualizar(request,1L);
        // Assert
        assertThat(entidad.getNombre()).isEqualTo("Juanes");
        assertThat(entidad.getEmail()).isEqualTo("juanes@escuela.com");
        verify(alumnoRepository).saveAndFlush(entidad);
    }
    @Test void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());
        // Act
        var error=assertThrows(RecursoNoEncontradoException.class,()->service.actualizar(request(),999L));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository,never()).saveAndFlush(any());
    }
    @Test void eliminar_sinInscripciones_eliminaAlumno() {
        // Arrange
        Alumnos entidad=alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        // Act
        service.eliminar(1L);
        // Assert
        verify(alumnoRepository).delete(entidad);
        verify(alumnoRepository).flush();
    }
    @Test void eliminar_conInscripciones_lanzaEntidadRelacionadaException() {
        // Arrange
        Alumnos entidad=alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(inscripcionRepository.existsByAlumnoId(1L)).thenReturn(true);
        // Act
        var error=assertThrows(EntidadRelacionadaException.class,()->service.eliminar(1L));
        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository,never()).delete(any());
    }
}
