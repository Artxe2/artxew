package artxew.project.layers.chat.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Create_Chat_ReqDto {

	@JsonIgnore
	@Schema(description = "일련번호")
	private Long sno;

	@JsonIgnore
	@Schema(description = "사용자번호")
	private Long userNo = SessionHelper.sno();

	@Schema(description = "이름")
	private String nm;

	@Schema(description = "대화상대 목록")
	private long[] itltList;
}