package artxew.project.layers.chat.dto.req;
import artxew.framework.decedent.dto.PageReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class QueryList_ChatBlck_ReqDto extends PageReqDto {

	@Schema(description = "요청사용자번호", example = "123")
	private Long reqUserNo;

	@Schema(description = "차단사용자번호", example = "456")
	private Long blckUserNo;
}