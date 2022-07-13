package artxew.project.layers.example.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ModifyExampleReqDto {

    @JsonIgnore
    private Long eid;

    private String txt;
}
