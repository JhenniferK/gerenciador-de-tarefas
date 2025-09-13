package br.edu.ifpb.es.gerenciador.exception;

public class TarefaException extends RuntimeException {

    public TarefaException(String message, Throwable cause) {
        super(message,cause);
    }

    public TarefaException(String message) {
        super(message);
    }

}
