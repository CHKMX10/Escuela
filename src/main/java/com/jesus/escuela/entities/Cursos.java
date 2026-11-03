package com.jesus.escuela.entities;


import com.jesus.escuela.utils.StringCustomUtils;
import com.jesus.escuela.utils.ValoresNumericosUtils;
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
    private Long id;

    @Column(name = "NOMBRE", length = 100, unique = true, nullable = false)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    private static void validarDatos(String nombre, String descripcion, Integer creditos){

        StringCustomUtils.validarTamanio(nombre, 5, 100, "El nombre es requerido y debe contener entre 5 y 100 caracteres");
        StringCustomUtils.validarTamanio(descripcion, 5, 200, "La descripcion es requerida y debe contener entre 5 y 200 caracteres");
        ValoresNumericosUtils.validarEnteroPositivo(creditos, "El credito debe contener 2 caracteres");
    }

    public void actualizar(String nombre, String descripcion, Integer creditos) {

        validarDatos(nombre, descripcion, creditos);

        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
        this.creditos = creditos;

    }

    public static Cursos crear(String nombre, String descripcion, Integer creditos){

        validarDatos(nombre, descripcion, creditos);

        return Cursos.builder()
                .nombre(nombre.trim())
                .descripcion(descripcion.trim())
                .creditos(creditos)
                .build();
    }

}
