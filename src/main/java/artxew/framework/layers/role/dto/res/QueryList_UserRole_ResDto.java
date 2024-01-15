package artxew.framework.layers.role.dto.res;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import artxew.framework.decedent.dto.PageResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
public class QueryList_UserRole_ResDto extends PageResDto<
QueryList_UserRole_ResDto.QueryItem
> {

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem {

		@NotNull
		@Schema(description = "일련번호", example = "1236")
		private Long sno;

		@NotEmpty
		@Schema(description = "사용자ID", example = "test1234")
		private String id;

		@NotEmpty
		@Schema(description = "이름", example = "테스트")
		private String nm;

		@Schema(description = "생년월일", example = "20001212")
		private String bday;

		@Schema(description = "전화번호", example = "01012345678")
		private String tel;

		@NotEmpty
		@Schema(description = "가입일자", example = "2020-02-02 20:20")
		private String joinDt;

		@JsonIgnore
		private String rolesStr;

		@Schema(description = "역할목록")
		private List<QueryList_Role_ResDto.QueryItem> roleList = new ArrayList<>();
	}
}