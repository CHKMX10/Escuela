package com.jesus.escuela.entities;


import com.jesus.escuela.enums.DiaSem;
import com.jesus.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Entity
@Table( name = "HORARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Horarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupos grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA", nullable = false)
    private DiaSem diaSem;

    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    public static Horarios crear(Grupos grupo, DiaSem diaSem,
                                 String horaInicio, String horaFin) {

        validarHoras(horaInicio, horaFin);

        return Horarios.builder()
                .grupo(grupo)
                .diaSem(diaSem)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .build();
    }

    public void actualizar(Grupos grupo, DiaSem diaSem,
                           String horaInicio, String horaFin) {

        validarHoras(horaInicio, horaFin);

        this.grupo = grupo;
        this.diaSem = diaSem;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    private static void validarHoras(String inicio, String fin) {
        try {
            LocalTime horaInicio = LocalTime.parse(inicio);
            LocalTime horaFin = LocalTime.parse(fin);

            if (!horaFin.isAfter(horaInicio)) {
                throw new IllegalArgumentException(
                        "La hora de fin debe ser posterior a la hora de inicio"
                );
            }
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Las horas deben tener formato HH:mm", e
            );
        }
    }

    public String obtenerHorarioFormateado() {
        return diaSem + " " + horaInicio + " " + horaFin;
    }

}
