package artxew.project.layers.chat.dto.req;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class MarkWrite_Chat_ReqDto {

	@Schema(description = "채팅번호")
	private Long chatNo;

	@Schema(description = "사용자번호")
	private Long userNo;

	@NotEmpty
	@Schema(description = "마지막채팅", example = "...")
	private String lastChat;
}