package com.jesus.escuela.enums;

import com.jesus.escuela.exceptions.RecursoNoEncontradoException;
import com.jesus.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSem {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado");

    private final String descripcion;

    public static DiaSem obtenerDiaPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (DiaSem diaSem : values()){
            if(StringCustomUtils.normalizarTexto(diaSem.descripcion)
                    .equalsIgnoreCase(descripcionNormalizada))
                return diaSem;
        }

        throw new RecursoNoEncontradoException("No existe un dia de la semana con la descripcion" + descripcion);
    }

}
