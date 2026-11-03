package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
import com.jesus.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private static void validarDatos(String nombre, Integer capacidad){

        StringCustomUtils.validarTamanio(nombre, 5, 50, "El nombre es requerido y debe contener entre 5 y 30 caracteres");
        ValoresNumericosUtils.validarEnteroPositivo(capacidad, "La capacidad debe contener 4 caracteres");
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
}
