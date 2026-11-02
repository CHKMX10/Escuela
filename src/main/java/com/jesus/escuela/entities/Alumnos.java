package com.jesus.escuela.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "ALUMNOS" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Alumnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long idAlumno;

    @Column(name = "NOMBRE", length =50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apPater;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apMater;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "MATRICULA", length = 10, nullable = false, unique = true)
    private String matricula;

    @Builder.Default
    @Column(name = "FECHA_INGRESO")
    private LocalDate dateIngreso = LocalDate.now();

    @Builder.Default//va a poner un valor por defecto o null con esta linea
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();


}
