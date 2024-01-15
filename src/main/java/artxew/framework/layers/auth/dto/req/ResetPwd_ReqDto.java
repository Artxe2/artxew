package artxew.framework.layers.auth.dto.req;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class ResetPwd_ReqDto implements Serializable {

	@JsonIgnore
	@Schema(description = "순번", example = "123456")
	private Long sno;

	@NotEmpty
	@Schema(description = "비밀번호", example = "test123!@#")
	private String pwd;

	@NotEmpty
	@Schema(description = "임시비밀번호", example = "abcdefg")
	private String tempPwd;
}