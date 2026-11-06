package com.jesus.escuela.service;

import com.jesus.escuela.dtos.grupos.*;
import com.jesus.escuela.entities.*;
import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.exceptions.*;
import com.jesus.escuela.mappers.GrupoMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.service.grupos.GrupoServiceImpl;
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
class GrupoServiceTest {
    @Mock AulaRepository aulaRepository;
    @Mock CursoRepository cursoRepository;
    @Mock GrupoRepository grupoRepository;
    @Mock HorarioRepository horarioRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock MaestrosRepository maestroRepository;
    @Mock GrupoMapper grupoMapper;
    @InjectMocks GrupoServiceImpl service;


    @Test
    void registrar_datosValidos_registraGrupo() {
        // Arrange
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-01");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro()));
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula()));
        GrupoResponse esperado = mock(GrupoResponse.class);
        when(grupoMapper.entidadAResponse(any(Grupos.class))).thenReturn(esperado);

        when(grupoRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        when(grupoMapper.requestAEntidad(any(),any(),any(),any())).thenAnswer(inv -> Grupos.crear(inv.getArgument(1),inv.getArgument(2),inv.getArgument(3),"2026-01"));

        // Act
        var resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Grupos> captura = org.mockito.ArgumentCaptor.forClass(Grupos.class);
        verify(grupoRepository).saveAndFlush(captura.capture());
        Grupos guardado = captura.getValue();
        assertThat(guardado.getCursos().getId()).isEqualTo(request.cursoId());
        assertThat(guardado.getMaestro().getId()).isEqualTo(request.maestroId());
        assertThat(guardado.getAulas().getId()).isEqualTo(request.aulaId());
        assertThat(guardado.getPeriodo()).isEqualTo(request.periodo());
        verify(grupoMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_periodoFormatoValido_registraGrupo() {
        // Arrange
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-01");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro()));
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula()));
        GrupoResponse esperado = mock(GrupoResponse.class);
        when(grupoMapper.entidadAResponse(any(Grupos.class))).thenReturn(esperado);

        when(grupoRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        when(grupoMapper.requestAEntidad(any(),any(),any(),any())).thenAnswer(inv -> Grupos.crear(inv.getArgument(1),inv.getArgument(2),inv.getArgument(3),"2026-01"));

        // Act
        var resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Grupos> captura = org.mockito.ArgumentCaptor.forClass(Grupos.class);
        verify(grupoRepository).saveAndFlush(captura.capture());
        Grupos guardado = captura.getValue();
        assertThat(guardado.getCursos().getId()).isEqualTo(request.cursoId());
        assertThat(guardado.getMaestro().getId()).isEqualTo(request.maestroId());
        assertThat(guardado.getAulas().getId()).isEqualTo(request.aulaId());
        assertThat(guardado.getPeriodo()).isEqualTo(request.periodo());
        verify(grupoMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_combinacionDuplicada_lanzaConflictoException() {
        // Arrange
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-01");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro()));
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula()));
        when(grupoRepository.existsByCursosIdAndMaestroIdAndAulasIdAndPeriodo(1L,1L,1L,"2026-01")).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(grupoRepository,never()).saveAndFlush(any());
    }

    @Test
    void registrar_cursoInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-01");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(grupoRepository,never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_grupoExistente_retornaGrupoConHorarios() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        GrupoResponse esperado = mock(GrupoResponse.class);
        when(grupoMapper.entidadAResponse(entidad)).thenReturn(esperado);

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
        verify(grupoRepository,never()).saveAndFlush(any());
    }

    @Test
    void listar_conGrupos_retornaLista() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findAll()).thenReturn(List.of(entidad));
        GrupoResponse esperado = mock(GrupoResponse.class);
        when(grupoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("Error de repositorio");
        when(grupoRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class,service::listar);

        // Assert
        assertThat(error).isSameAs(causa);
        verify(grupoRepository,never()).saveAndFlush(any());
    }

    @Test
    void actualizar_datosValidos_actualizaGrupo() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-02");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro()));
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula()));
        GrupoResponse esperado = mock(GrupoResponse.class);
        when(grupoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.actualizar(request,1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(grupoRepository).saveAndFlush(entidad);
    }

    @Test
    void actualizar_combinacionDeOtroGrupo_lanzaConflictoException() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        GrupoRequest request = new GrupoRequest(1L,1L,1L,"2026-02");
        Cursos curso = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro()));
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula()));
        when(grupoRepository.existsByCursosIdAndMaestroIdAndAulasIdAndPeriodoAndIdNot(1L,1L,1L,"2026-02",1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(grupoRepository,never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinInscripcionesNiHorarios_eliminaGrupo() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(grupoRepository).delete(entidad);
        verify(grupoRepository).flush();
    }

    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(inscripcionRepository.existsByGrupoId(1L)).thenReturn(true);

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class,() -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(grupoRepository,never()).saveAndFlush(any());
        verify(grupoRepository,never()).delete(any());
    }

    @Test
    void eliminar_conHorarios_lanzaEntidadRelacionadaException() {
        // Arrange
        Grupos entidad = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(horarioRepository.existsByGrupoId(1L)).thenReturn(true);

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class,() -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(grupoRepository,never()).saveAndFlush(any());
        verify(grupoRepository,never()).delete(any());
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

    private Grupos grupo() {
        return Grupos.builder().id(1L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
    }

    private Horarios horario() {
        return Horarios.builder().id(1L).grupo(grupo()).diaSem(DiaSem.LUNES).horaInicio("08:00").horaFin("10:00").build();
    }
}
