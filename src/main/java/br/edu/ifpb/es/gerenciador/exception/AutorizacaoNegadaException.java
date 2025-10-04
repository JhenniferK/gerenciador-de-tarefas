package br.edu.ifpb.es.gerenciador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AutorizacaoNegadaException extends RuntimeException {

    public AutorizacaoNegadaException(String message) {
        super(message);
    }

}
