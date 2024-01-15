package artxew.project.layers.chat.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import artxew.framework.decedent.dto.PageResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
public class QueryList_Chat_ResDto extends PageResDto<
QueryList_Chat_ResDto.QueryItem
> {

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem {

		@NotNull
		@Schema(description = "일련번호", example = "123456")
		private Long sno;

		@Schema(description = "이름", example = "abcdefg")
		private String nm;

		@NotEmpty
		@Schema(description = "마지막채팅", example = "abcdefg")
		private String lastChat;

		@NotEmpty
		@Schema(description = "수정일자", example = "1580642420000")
		private Long modDt;

		@Schema(description = "읽은시간", example = "1580642420000")
		private Long readTime;

		private long[] joinList;
		@Schema(description = "참여자목록")
		public void setJoinList(String str) {
			if (str == null || str.isEmpty()) {
				joinList = null;
			} else {
				String[] tokens = str.split(",");
				long[] joinList = new long[tokens.length];
				int index = 0;
				for (var userNo : tokens) {
					joinList[index++] = Long.valueOf(userNo);
				}
				this.joinList = joinList;
			}
		}
	}
}