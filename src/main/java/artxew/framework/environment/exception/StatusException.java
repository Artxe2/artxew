package artxew.framework.environment.exception;

import org.springframework.http.ResponseEntity;

import artxew.framework.decedent.dto.ServerResponseDto;

public final class StatusException extends RuntimeException {
    private final ServerResponseDto<Object> body;
    private final int status;

    public StatusException(ServerResponseDto<Object> data, int code) {
        super(String.valueOf(code));
        body = data;
        status = code;
    }

    public ResponseEntity<ServerResponseDto<Object>> parseResponse() {
        return ResponseEntity.status(status).body(body);
    }

    public int status() {
        return status;
    }
}