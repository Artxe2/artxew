package artxew.project.layers.chat.dto.res;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryNewList_Chat_ResDto {

	@JsonIgnore
	@Schema(example = "사용자번호")
	private Long userNo;

	private long[] chatList;
	@Schema(example = "신규 채팅 채팅번호 목록")
	public void setChatList(String str) {
		if (str == null || str.isEmpty()) {
			chatList = null;
		} else {
			String[] tokens = str.split(",");
			long[] chatList = new long[tokens.length];
			int index = 0;
			for (var chatNo : tokens) {
				chatList[index++] = Long.valueOf(chatNo);
			}
			this.chatList = chatList;
		}
	}
}
