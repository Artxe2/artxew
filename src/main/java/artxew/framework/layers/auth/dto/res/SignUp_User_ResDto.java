package artxew.framework.layers.auth.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUp_User_ResDto {

	@NotEmpty
	@Schema(description = "사용자일련번호", example = "1")
	private Long sno;

	@NotEmpty
	@Schema(description = "ID", example = "test1234")
	private String id;
}