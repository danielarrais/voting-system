package dev.danielarrais.votingsystem.api.handler;

import dev.danielarrais.votingsystem.api.dto.response.ErroResponse;
import dev.danielarrais.votingsystem.core.application.exceptions.NegocioException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErroResponse generalException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ErroResponse.builder()
                .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .mensagem(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(NegocioException.class)
    public ErroResponse negocioExceptionHandler(NegocioException ex) {
        log.error(ex.getMessage(), ex);
        return ErroResponse.builder()
                .codigo(HttpStatus.BAD_REQUEST.value())
                .mensagem(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ErroResponse bindValidationException(BindException ex) {
        log.error(ex.getMessage(), ex);

        BindingResult result = ex.getBindingResult();
        List<String> erros = this.extrairErrosDeValidacao(result);

        return ErroResponse.builder()
                .codigo(HttpStatus.BAD_REQUEST.value())
                .mensagem("Erros de validação")
                .erros(erros)
                .build();
    }

    private List<String> extrairErrosDeValidacao(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> erros = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            erros.add(fieldError.getDefaultMessage());
        }

        return erros;
    }

}
