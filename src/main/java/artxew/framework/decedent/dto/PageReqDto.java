package artxew.framework.decedent.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageReqDto {

    @ApiModelProperty(example = "1")
    @NotNull
    private Integer page;

    @NotNull
    @ApiModelProperty(example = "15")
    private Integer size;
}