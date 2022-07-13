package artxew.framework.environment.exception;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.flowlog.FlowLogHolder;
import artxew.framework.util.SessionMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public final class ExceptionResponseHandler implements ErrorController {

    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ServerResponseDto<Object>> notFoundHandler(
        NoHandlerFoundException e
    ) {
        HttpServletRequest request = SessionMap.request();
        String uri = (String) request.getAttribute(
            "javax.servlet.forward.request_uri"
        );

        if (uri == null) {
            uri = request.getRequestURI();
        }
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.notFoundHandler(%s)"
            , uri
        ));
        log.info("\n{}", FlowLogHolder.pop());
        return new DefinedException("not-found").parseResponse();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ServerResponseDto<Object>> methodNotAllowed(
        HttpRequestMethodNotSupportedException e
    ) {
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.methodNotAllowed(%s)"
            , SessionMap.request().getMethod()
        ));
        log.info("\n{}", FlowLogHolder.pop());
        return new DefinedException("method-not-allowed", e).parseResponse();
    }

    @ExceptionHandler(BindException.class)
    private ResponseEntity<ServerResponseDto<Object>> badRequestHandler(
        BindException e
    ) {
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.badRequestHandler(%d errors)"
            , e.getBindingResult().getErrorCount()
        ));
        FlowLogHolder.pop();
        return DefinedException.badRequest(e.getBindingResult()).parseResponse();
    }

    @ExceptionHandler(StatusException.class)
    private ResponseEntity<ServerResponseDto<Object>> statusHandler(
        StatusException e
    ) {
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.statusHandler(%d)"
            , e.status()
        ));
        FlowLogHolder.pop();
        return e.parseResponse();
    }

    @ExceptionHandler(DefinedException.class)
    private ResponseEntity<ServerResponseDto<Object>> definedHandler(
        DefinedException e
    ) {
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.definedHandler(%s)"
            , e.name()
        ));
        FlowLogHolder.pop();
        printError(e);
        return e.parseResponse();
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ServerResponseDto<Object>> undefinedHandler(Exception e) {
        FlowLogHolder.push(String.format(
            "ExceptionAdvice.undefinedHandler(%s)"
            , e.getClass().toString()
        ));
        FlowLogHolder.pop();
        printError(e);
        return DefinedException.unknownError(null, e).parseResponse();
    }

    private void printError(Exception e) {
        e.setStackTrace(Arrays.copyOf(e.getStackTrace(), 5));

        Throwable t = e.getCause();

        while (t != null) {
            t.setStackTrace(Arrays.copyOf(t.getStackTrace(), 5));
            t = t.getCause();
        }
        log.error("", e);
    }
}