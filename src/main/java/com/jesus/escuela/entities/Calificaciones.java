package com.jesus.escuela.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table( name = "CALIFICACIONES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Calificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    @Column(name = "CALIFICACION", nullable = false)
    private BigDecimal calificacion;

    @Builder.Default
    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaIngreso = LocalDate.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INSCRIPCION", nullable = false, unique = true)
    private Inscripcion inscripcion;

    public static Calificaciones crear(Inscripcion inscripcion, BigDecimal calificacion) {

        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción es requerida");
        }
        if (calificacion == null) {
            throw new IllegalArgumentException("La calificación es requerida");
        }

        return Calificaciones.builder()
                .inscripcion(inscripcion)
                .calificacion(calificacion)
                .fechaIngreso(LocalDate.now())
                .build();
    }

    public void actualizar(Inscripcion inscripcion, BigDecimal calificacion) {

        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción es requerida");
        }
        if (calificacion == null) {
            throw new IllegalArgumentException("La calificación es requerida");
        }

        this.inscripcion = inscripcion;
        this.calificacion = calificacion;
    }
}
