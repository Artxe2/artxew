package artxew.framework.layers.dmn.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class ModifyHgNm_Dmn_ReqDto {

	@JsonIgnore
	@Schema(description = "약어", example = "ABR")
	private String abr;

	@NotEmpty
	@Schema(description = "한글명, 복수 표기시 공백( )으로 분리", example = "약어")
	private String hgNm;
}