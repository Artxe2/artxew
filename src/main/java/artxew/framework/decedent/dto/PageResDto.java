package artxew.framework.decedent.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResDto<T> implements Serializable {

    private Integer count;
    
    private List<T> dataList;
}