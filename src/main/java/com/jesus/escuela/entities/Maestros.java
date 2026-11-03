package com.jesus.escuela.entities;

import com.jesus.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apMaterno;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "TELEFONO", length = 10, nullable = false, unique = true)
    private String telefono;

    @Builder.Default//es para una lista si no tiene lista
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private List<Grupos> grupos = new ArrayList<>();

    private static void validarDatos(String nombre, String apPaterno,
                                     String apMaterno, String email, String telefono){

        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apPaterno, 1, 50,
                    "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apMaterno, 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(email, 8, 100,
                "El Email es requerido y debe tener entre 8 y 100 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El numero de telefono es requerido y debe tener exactamente 10 digitos");
   }

   public String obtenerNombreCompleto(){

        return String.join("",
       this.getNombre(),
       this.getApPaterno(),
       this.getApMaterno());
   }
   public void actualizar(String nombre, String apPaterno,
                          String apMaterno, String email, String telefono){

        validarDatos(nombre, apPaterno, apMaterno, email, telefono);

        this.nombre = nombre.trim();
        this.apPaterno = apPaterno.trim();
        this.apMaterno = apMaterno.trim();
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
   }

    public static Maestros crear(String nombre, String apPaterno, String apMaterno,
                            String email, String telefono){

        validarDatos(nombre, apPaterno, apMaterno, email, telefono);

        return Maestros.builder()
                .nombre(nombre.trim())
                .apPaterno(apPaterno.trim())
                .apMaterno(apMaterno.trim())
                .email(email.trim().toLowerCase())
                .telefono(telefono.trim())
                .build();
    }
}
