package artxew.project.layers.menu.dto.res;
import java.io.Serializable;
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
public class QueryList_Menu_ResDto {
	private QueryList_Menu_ResDto() {
		throw new IllegalStateException("Dto namespace class");
	}
	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem implements Serializable {

		@NotNull
		@Schema(description = "일련번호", example = "123456")
		private Long sno;

		@NotEmpty
		@Schema(description = "부모메뉴번호", example = "123456")
		private Long prntMenuNo;

		@Schema(description = "이름", example = "사용자 관리")
		private String nm;

		@Schema(description = "설명", example = "사용자를 관리합니다.")
		private String expl;

		@Schema(description = "URL", example = "/adm/user")
		private String url;

		@Schema(description = "하위 메뉴")
		private List<QueryItem> chldList;
	}
}