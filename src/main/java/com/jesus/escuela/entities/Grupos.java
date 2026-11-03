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

    @Column(name = "PERIODO", length = 20, unique = true)
    private String periodo;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "grupo")
    private List<Horarios> horarios = new ArrayList<>();


    private static void validarDatos(String periodo){
        StringCustomUtils.validarTamanio(periodo, 8, 20, "El peridodo debe tener entre 8 y 20 caracteres");
    }

    public void actualizar(Cursos cursos, Maestros maestros,
                           Aulas aulas, String periodo) {

        validarDatos(periodo);

        this.cursos = cursos;
        this.maestro = maestros;
        this.aulas = aulas;
        this.periodo = periodo.trim();
    }

    public static Grupos crear(Cursos cursos, Maestros maestros,
                         Aulas aulas, String periodo){

        validarDatos(periodo);

        return Grupos.builder()
                .cursos(cursos)
                .maestro(maestros)
                .aulas(aulas)
                .periodo(periodo.trim())
                .build();
    }


}
