package com.jesus.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
    private Long idGrup;

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




}
