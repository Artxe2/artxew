package artxew.project.layers.chat.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Create_ChatBlck_ReqDto {

	@JsonIgnore
	@Schema(description = "요청사용자번호", example = "123456")
	private Long reqUserNo = SessionHelper.sno();

	@NotNull
	@Schema(description = "차단사용자번호", example = "123456")
	private Long blckUserNo;

	@NotNull
	@Schema(description = "채팅번호", example = "123456")
	private Long chatNo;

	@NotEmpty
	@Schema(description = "차단사유", example = "abcdefg")
	private String blckRsn;
}