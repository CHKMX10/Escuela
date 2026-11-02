package com.jesus.escuela.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "MAESTROS" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Maestros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAESTRO")
    private Long idMaestro;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apPaterMestro;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apMaterMaestro;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String emailMaestro;

    @Column(name = "TELEFONO", length = 10, nullable = false, unique = true)
    private String numTelefono;

    @Builder.Default//es para una lista si no tiene lista
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private List<Grupos> grupos = new ArrayList<>();

}
