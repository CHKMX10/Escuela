package com.jesus.escuela.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "CURSOS" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Cursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long idCurso;

    @Column(name = "NOMBRE", length = 100, unique = true, nullable = false)
    private String nombreCurso;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcionCurso;

    @Column(name = "CREDITOS", nullable = false)
    private int creditos;

}
