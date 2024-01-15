package artxew.project.layers.chat.dto.req;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Query_Chat_ReqDto {

	@Schema(description = "채팅번호")
	private Long chatNo;

	@Schema(description = "사용자번호")
	private Long userNo = SessionHelper.sno();
}