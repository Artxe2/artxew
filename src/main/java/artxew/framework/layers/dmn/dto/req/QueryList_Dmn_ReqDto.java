package artxew.framework.layers.dmn.dto.req;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class QueryList_Dmn_ReqDto {

	@Schema(description = "약어", example = "ABR")
	private String abr;

	@Schema(description = "영문명", example = "abbreviation")
	private String engNm;

	@Schema(description = "한글명, 복수 표기시 공백( )으로 분리", example = "약어")
	private String hgNm;
}