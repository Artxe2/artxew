package artxew.framework.layers.role.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Create_UserRole_ReqDto {

	@JsonIgnore
	@Schema(description = "사용자번호", example = "1")
	private Long userNo;

	@NotEmpty
	@Schema(description = "역할코드", example = "01")
	private String roleCd;
}