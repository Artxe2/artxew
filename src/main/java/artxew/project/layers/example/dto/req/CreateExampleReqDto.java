package artxew.project.layers.example.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CreateExampleReqDto {

    @JsonIgnore
    private Long eid;

    private String txt;
}
