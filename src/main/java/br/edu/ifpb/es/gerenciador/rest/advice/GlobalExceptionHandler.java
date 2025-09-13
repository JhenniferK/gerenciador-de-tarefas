package br.edu.ifpb.es.gerenciador.rest.advice;

import br.edu.ifpb.es.gerenciador.exception.AuthorizationDeniedException;
import br.edu.ifpb.es.gerenciador.exception.EstadoInvalidoException;
import br.edu.ifpb.es.gerenciador.exception.JwtTokenException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static enum ErrorType {
        ERRO_INESPERADO,
        REQUISICAO_INVALIDA,
        ESTADO_INVALIDO,
        ERRO_DE_VALIDACAO,
        ACESSO_NEGADO,
        NAO_AUTORIZADO
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        return buildProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ERRO_INESPERADO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildProblemDetail(exception, HttpStatus.BAD_REQUEST, ErrorType.REQUISICAO_INVALIDA);
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail handleEstadoInvalidoException(EstadoInvalidoException exception) {
        return buildProblemDetail(exception, HttpStatus.BAD_REQUEST, ErrorType.ESTADO_INVALIDO);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.of(handleMethodArgumentNotValidException(exception)).build();
    }

    private ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ProblemDetail problemDetail = buildProblemDetail(exception, HttpStatus.BAD_REQUEST, ErrorType.ERRO_DE_VALIDACAO);
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        return buildProblemDetail(exception, HttpStatus.FORBIDDEN, ErrorType.ACESSO_NEGADO);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ProblemDetail handleJwtTokenException(JwtTokenException exception) {
        return buildProblemDetail(exception, HttpStatus.UNAUTHORIZED, ErrorType.NAO_AUTORIZADO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleJwtTokenException(BadCredentialsException exception) {
        return buildProblemDetail(exception, HttpStatus.UNAUTHORIZED, ErrorType.NAO_AUTORIZADO);
    }

    private ProblemDetail buildProblemDetail(Exception exception, HttpStatus status, ErrorType type) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getLocalizedMessage());
        problemDetail.setType(URI.create(type.name()));
        problemDetail.setProperty("trace", stackTraceToString(exception));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    private String stackTraceToString(Exception exception) {
        StringWriter errors = new StringWriter();
        exception.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
