package artxew.framework.layers.img.dto.req;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Move_Img_ReqDto {

	@NotEmpty
	@Schema(description = "구분코드", example = "01")
	private String clCd;

	@NotEmpty
	@Schema(description = "참조키", example = "123456")
	private String refKey;

	@NotEmpty
	@Schema(description = "참조키 TOBE", example = "123456")
	private String moveKey;
}