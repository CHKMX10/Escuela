package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
import com.jesus.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* CUANDO SON MUCHAS RELACIONES CON LLAVES FORANEAS Y UNICAS
*
* */
@Entity
@Table( name = "GRUPOS" , uniqueConstraints = @UniqueConstraint(
        name = "GRUPO_CU_MA_AU_PE_UK",
        columnNames = {"ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO" }
))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Grupos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Cursos cursos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestros maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aulas aulas;

    @Column(name = "PERIODO", length = 20)
    private String periodo;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "grupo")
    private List<Horarios> horarios = new ArrayList<>();

    private static void validarDatos(String periodo) {
        if (periodo == null || !periodo.matches("^\\d{4}-(0[1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("El periodo debe tener el formato yyyy-MM");
        }
    }

    public void actualizar(Cursos cursos, Maestros maestros,
                           Aulas aulas, String periodo) {
        validarDatos(periodo);

        this.cursos = cursos;
        this.maestro = maestros;
        this.aulas = aulas;
        this.periodo = periodo.trim();
    }
    public boolean cambioEnDatos(String periodo, Aulas aula, Maestros maestro, Cursos curso) {
        return !Objects.equals(this.periodo, periodo) ||
                !Objects.equals(this.aulas.getId(), aula.getId()) ||
                !Objects.equals(this.maestro.getId(), maestro.getId()) ||
                !Objects.equals(this.cursos.getId(), curso.getId());
    }

    public static Grupos crear(Cursos curso,  Maestros maestro, Aulas aula,String periodo){

        validarDatos(periodo);

        return Grupos.builder()
                .cursos(curso)
                .maestro(maestro)
                .aulas(aula)
                .periodo(periodo.trim())
                .build();
    }

    public boolean tieneHorarios() {
        return horarios != null && !horarios.isEmpty();
    }


}
