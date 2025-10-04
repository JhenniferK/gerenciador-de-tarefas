package br.edu.ifpb.es.gerenciador.exception;

public class JwtTokenException extends NaoEncontradoException {

    public JwtTokenException(String message) {
        super(message);
    }

}
