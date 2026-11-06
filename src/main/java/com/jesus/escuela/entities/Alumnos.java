package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


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

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apMaterno;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;//este viene de la base de datos

    @Column(name = "MATRICULA", length = 10, nullable = false, unique = true)
    private String matricula;//esta viene de la base de datos

    @Builder.Default
    @Column(name = "FECHA_INGRESO")
    private LocalDate dateIngreso = LocalDate.now();//fecha no se valida y se ocupa la de la base de datos

    @Builder.Default//va a poner un valor por defecto o null con esta linea
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    private static void validarDatos(String nombre, String apPaterno, String apMaterno) {

        StringCustomUtils.validarTamanio(nombre, 3, 50, "El nombre es requerido y debe contener entre 5 y 30 caracteres");
        StringCustomUtils.validarTamanio(apPaterno, 5, 50, "El apellido paterno es requerido y debe contener entre 5 y 150 caracteres");
        StringCustomUtils.validarTamanio(apMaterno, 5, 50, "El apellido materno es requerido y debe contener entre 5 y 150 caracteres");
    }

    public void asignarDatosAcademicos(String email, String matricula) {

        StringCustomUtils.validarTamanio(email, 1, 100, "El email es requerido y debe tener entre 1 y 100 caracteres");
        StringCustomUtils.validarTamanio(matricula, 10, 10, "La matricula es requerida y debe tener 10 caracteres");

        this.email = email.toLowerCase().trim();
        this.matricula = matricula.trim();
    }

    public boolean cambioEnDatos(String nombre, String apPaterno, String apMaterno) {

        validarDatos(nombre, apPaterno, apMaterno);

        return !this.nombre.equals(nombre) ||
                !this.apPaterno.equals(apPaterno) ||
                !this.apMaterno.equals(apMaterno);

    }
    public String obtenerNombreCompleto(){

        return String.join("",
                this.getNombre(),
                this.getApPaterno(),
                this.getApMaterno());
    }

    public BigDecimal calcularPromedio(){
        List<BigDecimal> calificaciones = inscripciones.stream()
                .map(Inscripcion::getCalificaciones)
                .filter(Objects::nonNull)
                .map(Calificaciones::getCalificacion)
                .filter(Objects::nonNull)
                .toList();

        if (calificaciones.isEmpty())
            return BigDecimal.ZERO;

        BigDecimal suma = calificaciones.stream()
                .reduce(BigDecimal.ZERO , BigDecimal::add);
        return suma.divide(BigDecimal.valueOf(calificaciones.size()), 2, RoundingMode.HALF_UP);
    }

    public void actualizar(String nombre, String apPaterno, String apMaterno,
                           String email, String matricula) {

        validarDatos(nombre, apPaterno, apMaterno);

        asignarDatosAcademicos(email, matricula);

        this.nombre = nombre.trim();
        this.apPaterno = apPaterno.trim();
        this.apMaterno = apMaterno.trim();
    }

    public static Alumnos crear(String nombre, String apPaterno, String apMaterno) {

        validarDatos(nombre, apPaterno, apMaterno);

        return Alumnos.builder()
                .nombre(nombre.trim())
                .apPaterno(apPaterno.trim())
                .apMaterno(apMaterno.trim())
                .build();
    }


}
