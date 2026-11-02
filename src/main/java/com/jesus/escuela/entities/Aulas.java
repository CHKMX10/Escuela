package com.jesus.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "AULAS" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Aulas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA", nullable = false)
    private Long idAula;

    @Column(name = "NOMBRE", length = 30, nullable = false, unique = true)
    private String nombre;

    @Column(name = "CAPACIDAD", length = 4, nullable = false)
    private int capacidad;

}
