package br.edu.ifpb.es.gerenciador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String message, Throwable cause) {
        super(message,cause);
    }

    public NaoEncontradoException(String message) {
        super(message);
    }

}
