package artxew.project.layers.chat.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Create_Chat_ResDto {

	@NotNull
	@Schema(description = "일련번호", example = "123456")
	private Long sno;
}