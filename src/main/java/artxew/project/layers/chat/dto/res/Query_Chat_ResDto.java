package artxew.project.layers.chat.dto.res;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Query_Chat_ResDto {

	@NotNull
	@Schema(description = "일련번호", example = "123456")
	private Long sno;

	@Schema(description = "이름", example = "채팅이름")
	private String nm;

	@NotEmpty
	@Schema(description = "마지막채팅", example = "...")
	private String lastChat;

	@NotEmpty
	@Schema(description = "수정일자", example = "1580642420000")
	private Long modDt;

	@Schema(description = "읽은시간", example = "1580642420000")
	private Long readTime;

	@Schema(description = "참여자목록")
	private List<JoinUser> joinList;

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class JoinUser {

		@NotNull
		@Schema(description = "사용자번호", example = "123456")
		private Long userNo;

		@NotEmpty
		@Schema(description = "사용자ID", example = "user1234")
		private String userId;

		@NotEmpty
		@Schema(description = "사용자명", example = "사용자")
		private String userNm;

		@NotNull
		@Schema(description = "읽은시간", example = "1580642420000")
		private Long readTime;
	}
}