package artxew.framework.decedent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import artxew.framework.environment.exception.StatusException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponseDto<T> {

    @ApiModelProperty(example = "{\"key\": \"value\"}")
    @JsonProperty("DATA")
    private T data;

    @ApiModelProperty(example = "OK")
    @JsonProperty("MESSAGE")
    private String message;

    @ApiModelProperty(example = "forbidden")
    @JsonProperty("ERROR")
    private String error;

    public ServerResponseDto(T data) {
        this.data = data;
    }

    public ServerResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ServerResponseDto(T data, String message, String error) {
        this.data = data;
        this.message = message;
        this.error = error;
    }

    @SuppressWarnings("unchecked")
    public ServerResponseDto<T> status(int code) throws StatusException {
        status(new StatusException((ServerResponseDto<Object>) this, code));
        return this;
    }

    private void status(StatusException e) {
        throw e;
    }
}