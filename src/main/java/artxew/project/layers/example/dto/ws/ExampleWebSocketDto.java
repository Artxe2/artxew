package artxew.project.layers.example.dto.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExampleWebSocketDto {

    @JsonIgnore
    public static final int CONNECT = 0;

    @JsonIgnore
    public static final int IN = 1;

    @JsonIgnore
    public static final int MESSAGE = 2;

    @JsonIgnore
    public static final int ERROR = 8;

    @JsonIgnore
    public static final int OUT = 9;

    private String id;

    private int protocol;

    private Object data;
}