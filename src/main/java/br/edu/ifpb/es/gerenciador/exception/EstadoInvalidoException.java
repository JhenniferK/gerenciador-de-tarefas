package br.edu.ifpb.es.gerenciador.exception;

public class EstadoInvalidoException extends NaoEncontradoException {

    public EstadoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EstadoInvalidoException(String message) {
        super(message);
    }

}
