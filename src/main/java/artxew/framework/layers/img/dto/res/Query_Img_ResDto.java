package artxew.framework.layers.img.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Query_Img_ResDto {

	@NotEmpty
	@Schema(description = "구분코드", example = "01")
	private String clCd;

	@NotEmpty
	@Schema(description = "참조키", example = "abcdefg")
	private String refKey;

	@NotEmpty
	@Schema(description = "대체텍스트", example = "대체텍스트")
	private String alt;

	@NotEmpty
	@Schema(description = "확장자", example = "png")
	private String exts;
}