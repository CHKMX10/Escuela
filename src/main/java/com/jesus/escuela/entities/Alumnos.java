package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
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
    private Long id;

    @Column(name = "NOMBRE", length =50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apMaterno;

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

    private static void validarDatos(String nombre, String apPaterno, String apMaterno, LocalDate dateIngreso){

        StringCustomUtils.validarTamanio(nombre, 5, 50, "El nombre es requerido y debe contener entre 5 y 30 caracteres");
        StringCustomUtils.validarTamanio(apPaterno, 10, 50, "El apellido paterno es requerido y debe contener entre 5 y 150 caracteres");
        StringCustomUtils.validarTamanio(apMaterno, 10, 50, "El apellido materno es requerido y debe contener entre 5 y 150 caracteres");
        StringCustomUtils.validarTamanio(StringCustomUtils.localDateAString(dateIngreso), 1, 10, "El apellido materno es requerido y debe contener 10 caracteres");

    }

    public void actualizar(String nombre, String apPaterno, String apMaterno,
                           String email, String matricula, LocalDate dateIngreso) {

        validarDatos(nombre, apPaterno, apMaterno, dateIngreso);

        this.nombre = nombre.trim();
        this.apPaterno = apPaterno.trim();
        this.apMaterno = apMaterno.trim();
        this.email = email.trim();
        this.matricula = matricula.trim();
        this.dateIngreso = dateIngreso;
    }

public static Alumnos crear(String nombre, String apPaterno, String apMaterno,
                             String email, String matricula, LocalDate dateIngreso){

    validarDatos(nombre, apPaterno, apMaterno, dateIngreso);

    return Alumnos.builder()
            .nombre(nombre.trim())
            .apPaterno(apPaterno.trim())
            .apMaterno(apMaterno.trim())
            .email(email)
            .matricula(matricula)
            .dateIngreso(dateIngreso)
            .build();
}
}
