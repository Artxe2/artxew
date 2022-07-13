package artxew.project.layers.example.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryExampleInfoResDto {
        
    private Integer rno;

    private Long eid;

    private String txt;

    private String crtTm;

    private String udtTm;
}
