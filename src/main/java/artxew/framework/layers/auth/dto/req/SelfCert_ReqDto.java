package artxew.framework.layers.auth.dto.req;
import java.io.Serializable;
import artxew.project.enums.StringYN;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class SelfCert_ReqDto implements Serializable {

	@NotEmpty
	@Schema(example = "abcdefg")
	private String ci;

	@NotEmpty
	@Schema(example = "abcdefg")
	private String di;

	@NotEmpty
	@Schema(description = "이름", example = "테스트")
	private String nm;

	@NotEmpty
	@Schema(description = "생년월일", example = "20001212")
	private String bday;

	@NotEmpty
	@Schema(description = "전화번호", example = "01012345678")
	private String tel;

	@NotEmpty
	@Schema(description = "성별코드", example = "M")
	private String gndrCd;

	@NotNull
	@Schema(description = "외국인여부", example = "N")
	private StringYN frgnYn;
}