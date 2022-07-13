package artxew.framework.layers.oauth.dto.res;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySessionResDto implements Serializable {

    @ApiModelProperty(example = "test123")
    private String usrId;

    @ApiModelProperty(example = "01,02,03")
    private String auth;
}