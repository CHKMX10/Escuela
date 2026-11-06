package com.jesus.escuela.service;

import com.jesus.escuela.dtos.horario.*;
import com.jesus.escuela.entities.*;
import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.exceptions.*;
import com.jesus.escuela.mappers.HorarioMapper;
import com.jesus.escuela.repositories.*;
import com.jesus.escuela.service.horario.HorarioServiceImpl;
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
class HorarioServiceTest {
    @Mock GrupoRepository grupoRepository;
    @Mock HorarioRepository horarioRepository;
    @org.mockito.Spy HorarioMapper horarioMapper = new HorarioMapper();
    @InjectMocks HorarioServiceImpl service;


    @Test
    void registrar_datosValidos_registraHorario() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));
        HorarioResponse esperado = mock(HorarioResponse.class);
        lenient().when(horarioMapper.entidadAResponse(any(Horarios.class))).thenReturn(esperado);

        when(horarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Horarios> captura = org.mockito.ArgumentCaptor.forClass(Horarios.class);
        verify(horarioRepository).save(captura.capture());
        Horarios guardado = captura.getValue();
        assertThat(guardado.getGrupo().getId()).isEqualTo(request.idGrupo());
        assertThat(guardado.getHoraInicio()).isEqualTo(request.horaInicio());
        assertThat(guardado.getHoraFin()).isEqualTo(request.horaFin());
        verify(horarioMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_mismoGrupoDatosIgualesExceptoPeriodo_registraExitosamente() {
        // Arrange
        HorarioRequest request = new HorarioRequest(2L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        destino = Grupos.builder().id(2L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-02").build();
        Horarios anterior = horario();
        lenient().when(horarioRepository.buscarTraslapesPorGrupo(anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenAnswer(inv -> java.util.Map.of(1L,List.of(anterior)).getOrDefault(inv.getArgument(0),List.of()));
        lenient().when(horarioRepository.buscarTraslapesPorAulaYPeriodo(eq(1L),anyString(),anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenAnswer(inv -> java.util.Map.of("2026-01",List.of(anterior)).getOrDefault(inv.getArgument(1),List.of()));
        when(grupoRepository.findById(2L)).thenReturn(Optional.of(destino));
        HorarioResponse esperado = mock(HorarioResponse.class);
        lenient().when(horarioMapper.entidadAResponse(any(Horarios.class))).thenReturn(esperado);

        when(horarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.registrar(request);

        // Assert
        verify(horarioRepository).buscarTraslapesPorAulaYPeriodo(eq(1L),eq("2026-02"),anyLong(),any(),anyString(),anyString(),nullable(Long.class));
        assertThat(resultado).isSameAs(esperado);
        org.mockito.ArgumentCaptor<Horarios> captura = org.mockito.ArgumentCaptor.forClass(Horarios.class);
        verify(horarioRepository).save(captura.capture());
        Horarios guardado = captura.getValue();
        assertThat(guardado.getGrupo().getId()).isEqualTo(request.idGrupo());
        assertThat(guardado.getHoraInicio()).isEqualTo(request.horaInicio());
        assertThat(guardado.getHoraFin()).isEqualTo(request.horaFin());
        verify(horarioMapper).entidadAResponse(guardado);
    }

    @Test
    void registrar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","10:00","08:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void registrar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","08:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void registrar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));
        Grupos ocupado = grupo();
        ocupado = Grupos.builder().id(3L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
        Horarios existente = Horarios.crear(ocupado,DiaSem.LUNES,"09:00","11:00");
        existente = Horarios.builder().id(3L).grupo(ocupado).diaSem(DiaSem.LUNES).horaInicio("09:00").horaFin("11:00").build();
        lenient().when(horarioRepository.buscarTraslapesPorAulaYPeriodo(eq(1L),eq("2026-01"),anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenReturn(List.of(existente));

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void registrar_traslapeEnElMismoGrupo_lanzaConflictoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));
        lenient().when(horarioRepository.buscarTraslapesPorGrupo(eq(1L),any(),anyString(),anyString(),nullable(Long.class))).thenReturn(List.of(horario()));

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void obtenerPorId_horarioExistente_retornaHorario() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorarioResponse esperado = mock(HorarioResponse.class);
        when(horarioMapper.entidadAResponse(entidad)).thenReturn(esperado);

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
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void listar_conHorarios_retornaLista() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findAll()).thenReturn(List.of(entidad));
        HorarioResponse esperado = mock(HorarioResponse.class);
        when(horarioMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        var resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("Error de repositorio");
        when(horarioRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class,service::listar);

        // Assert
        assertThat(error).isSameAs(causa);
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void actualizar_datosValidos_actualizaHorario() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));
        lenient().when(horarioRepository.buscarTraslapesPorGrupo(eq(1L),any(),anyString(),anyString(),nullable(Long.class))).thenReturn(List.of());
        lenient().when(horarioRepository.buscarTraslapesPorAulaYPeriodo(eq(1L),eq("2026-01"),anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenReturn(List.of());
        HorarioResponse esperado = mock(HorarioResponse.class);
        when(horarioMapper.entidadAResponse(entidad)).thenReturn(esperado);

        when(horarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.actualizar(request,1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).save(entidad);
    }

    @Test
    void actualizar_mismoGrupoDatosIgualesExceptoPeriodo_actualizaExitosamente() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorarioRequest request = new HorarioRequest(2L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        destino = Grupos.builder().id(2L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-02").build();
        Horarios anterior = horario();
        lenient().when(horarioRepository.buscarTraslapesPorGrupo(anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenAnswer(inv -> java.util.Map.of(1L,List.of(anterior)).getOrDefault(inv.getArgument(0),List.of()));
        lenient().when(horarioRepository.buscarTraslapesPorAulaYPeriodo(eq(1L),anyString(),anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenAnswer(inv -> java.util.Map.of("2026-01",List.of(anterior)).getOrDefault(inv.getArgument(1),List.of()));
        when(grupoRepository.findById(2L)).thenReturn(Optional.of(destino));
        HorarioResponse esperado = mock(HorarioResponse.class);
        when(horarioMapper.entidadAResponse(entidad)).thenReturn(esperado);

        when(horarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.actualizar(request,1L);

        // Assert
        verify(horarioRepository).buscarTraslapesPorAulaYPeriodo(eq(1L),eq("2026-02"),anyLong(),any(),anyString(),anyString(),nullable(Long.class));
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).save(entidad);
    }

    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorarioRequest request = new HorarioRequest(1L,"Lunes","10:00","08:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void actualizar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");
        Grupos destino = grupo();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(destino));
        Grupos ocupado = grupo();
        ocupado = Grupos.builder().id(3L).cursos(curso()).maestro(maestro()).aulas(aula()).periodo("2026-01").build();
        Horarios existente = Horarios.crear(ocupado,DiaSem.LUNES,"09:00","11:00");
        existente = Horarios.builder().id(3L).grupo(ocupado).diaSem(DiaSem.LUNES).horaInicio("09:00").horaFin("11:00").build();
        lenient().when(horarioRepository.buscarTraslapesPorAulaYPeriodo(eq(1L),eq("2026-01"),anyLong(),any(),anyString(),anyString(),nullable(Long.class))).thenReturn(List.of(existente));

        // Act
        ConflictoException error = assertThrows(ConflictoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        HorarioRequest request = new HorarioRequest(1L,"Lunes","08:00","10:00");

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.actualizar(request,1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
    }

    @Test
    void eliminar_horarioExistente_eliminaHorario() {
        // Arrange
        Horarios entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(horarioRepository).delete(entidad);
        
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange


        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class,() -> service.eliminar(999L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository,never()).save(any());
        verify(horarioRepository,never()).delete(any());
    }

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
}

