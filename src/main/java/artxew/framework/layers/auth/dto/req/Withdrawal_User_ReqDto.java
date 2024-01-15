package artxew.framework.layers.auth.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Withdrawal_User_ReqDto {

	@JsonIgnore
	@Schema(description = "일련번호", example = "123")
	private Long sno = SessionHelper.sno();

	@JsonIgnore
	@Schema(description = "CI")
	private String ci;

	@NotEmpty
	@Schema(description = "탈퇴사유", example = "탈퇴")
	private String wdrwRsn;
}