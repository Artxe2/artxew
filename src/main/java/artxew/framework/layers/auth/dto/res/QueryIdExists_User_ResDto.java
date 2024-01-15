package artxew.framework.layers.auth.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import artxew.project.enums.StringYN;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryIdExists_User_ResDto {

	@NotEmpty
	@Schema(description = "회원ID", example = "user1234")
	private String userId;

	@NotNull
	@Schema(description = "존재여부", example = "Y")
	private StringYN exstYn;
}