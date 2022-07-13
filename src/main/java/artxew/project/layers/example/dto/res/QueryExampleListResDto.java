package artxew.project.layers.example.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;

import artxew.framework.decedent.dto.PageResDto;
import lombok.Data;

public class QueryExampleListResDto extends PageResDto<
    QueryExampleListResDto.QueryItem
> {
    
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QueryItem {
        
        private Integer rno;

        private Long eid;

        private String txt;

        private String crtTm;

        private String udtTm;
    }
}
