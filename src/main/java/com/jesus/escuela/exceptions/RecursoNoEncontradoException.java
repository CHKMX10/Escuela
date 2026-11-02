package com.jesus.escuela.exceptions;


/*
 * CONVERTIR ERRORES EN RESPUESTAS
 *
 * EXCEPCIONES PROPIAS
 *
 * @EXCEPTIONHANDLER
 * */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
