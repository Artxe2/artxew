package artxew.project.layers.chat.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.decedent.dto.PageReqDto;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class QueryList_Chat_ReqDto extends PageReqDto {

	@JsonIgnore
	@Schema(description = "사용자번호", example = "1234")
	private Long userNo = SessionHelper.sno();
}