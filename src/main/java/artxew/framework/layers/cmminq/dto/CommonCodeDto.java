package artxew.framework.layers.cmminq.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonCodeDto implements Serializable {

    @ApiModelProperty(example = "USR_AUTH")
    private String cdGrp;

    @ApiModelProperty(example = "01")
    private String cdv;

    @ApiModelProperty(example = "ADMIN")
    private String cdnm;
}
