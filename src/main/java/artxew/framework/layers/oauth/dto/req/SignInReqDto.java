package artxew.framework.layers.oauth.dto.req;

import javax.validation.constraints.NotEmpty;

import artxew.framework.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignInReqDto {

    @ApiModelProperty(example = "test123")
    @NotEmpty
    private String usrId;

    @ApiModelProperty(example = "1234")
    @NotEmpty
    private String pwd;

    public void setPwd(String pwd) {
        this.pwd = StringUtil.toHash(pwd);
    }
}