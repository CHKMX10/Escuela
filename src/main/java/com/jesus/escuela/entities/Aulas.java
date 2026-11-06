package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
import com.jesus.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table( name = "AULAS" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Aulas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long id;

    @Column(name = "NOMBRE", length = 30, nullable = false, unique = true)
    private String nombre;

    @Column(name = "CAPACIDAD", length = 4, nullable = false)
    private Integer capacidad;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aulas")
    private List<Grupos> grupos = new ArrayList<>();

    private static void validarDatos(String nombre, Integer capacidad){

        StringCustomUtils.validarTamanio(nombre, 5, 30, "El nombre es requerido y debe contener entre 5 y 30 caracteres");
        ValoresNumericosUtils.validarEnteroPositivo(capacidad, "La capacidad debe ser un número positivo");
    }

    public void actualizar(String nombre, Integer capacidad) {

        validarDatos(nombre, capacidad);

        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }

    public static Aulas crear(String nombre, Integer capacidad){

        validarDatos(nombre, capacidad);

        return Aulas.builder()
                .nombre(nombre.trim())
                .capacidad(capacidad)
                .build();
    }

    public boolean cambioEnDatos(String nombre, Integer capacidad) {

        validarDatos(nombre, capacidad);

        return !this.nombre.equals(nombre) ||
                !this.capacidad.equals(capacidad);

    }
}
