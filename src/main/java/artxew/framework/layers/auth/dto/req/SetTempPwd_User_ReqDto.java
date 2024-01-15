package artxew.framework.layers.auth.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class SetTempPwd_User_ReqDto {

	@JsonIgnore
	@Schema(description = "순번", example = "123456")
	private Long sno;

	@NotEmpty
	@Schema(example = "test1234")
	private String id;

	@JsonIgnore
	@Schema(example = "abcdefg")
	private String ci;

	@JsonIgnore
	@Schema(description = "임시비밀번호", example = "abcdefg")
	private String tempPwd = StringUtil.uniqueKey(20);

	@JsonIgnore
	@Schema(description = "메일주소", example = "test@test.test")
	private String mail;
}