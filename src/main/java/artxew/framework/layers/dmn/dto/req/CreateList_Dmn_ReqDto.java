package artxew.framework.layers.dmn.dto.req;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class CreateList_Dmn_ReqDto {

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CreateItem {

		@NotEmpty
		@Schema(description = "약어", example = "ABR")
		private String abr;

		@NotEmpty
		@Schema(description = "영문명", example = "abbreviation")
		private String engNm;

		@NotEmpty
		@Schema(description = "한글명, 복수 표기시 공백( )으로 분리", example = "약어")
		private String hgNm;
	}
	@NotEmpty
	@Schema(description = "도메인 목록")
	private CreateItem[] items;
}