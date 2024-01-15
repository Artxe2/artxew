package artxew.framework.layers.auth.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class SignIn_User_ReqDto {

	@NotEmpty
	@Schema(example = "test1234")
	private String id;

	@NotEmpty
	@Schema(description = "비밀번호", example = "test123!@#")
	private String pwd;

	@JsonIgnore
	private Long sno;

	@JsonIgnore
	private String ip;
}