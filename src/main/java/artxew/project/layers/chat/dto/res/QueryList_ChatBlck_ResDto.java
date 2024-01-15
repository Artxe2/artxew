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
public class QueryList_ChatBlck_ResDto extends PageResDto<
QueryList_ChatBlck_ResDto.QueryItem
> {

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem {

		@NotNull
		@Schema(description = "요청사용자번호", example = "123456")
		private Long reqUserNo;

		@NotNull
		@Schema(description = "차단사용자번호", example = "123456")
		private Long blckUserNo;

		@NotNull
		@Schema(description = "채팅번호", example = "123456")
		private Long chatNo;

		@NotEmpty
		@Schema(description = "차단사유", example = "abcdefg")
		private String blckRsn;

		@NotEmpty
		@Schema(description = "등록일자", example = "2020-02-02 20:20:20")
		private String regDt;
	}
}