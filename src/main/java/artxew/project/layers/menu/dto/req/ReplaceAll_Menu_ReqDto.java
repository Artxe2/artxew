package artxew.project.layers.menu.dto.req;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class ReplaceAll_Menu_ReqDto {

	@NotEmpty
	private List<CreateItem> data = new ArrayList<>();
	public void setData(CreateItem[] data) {
		recursion(data, 0, 1, null);
	}
	private long recursion(CreateItem[] data, int index, long sno, Long prntMenuNo) {
		while (index < data.length) {
			CreateItem item = data[index++];
			this.data.add(item);
			item.setSno(sno++);
			item.setPrntMenuNo(prntMenuNo);
			if (item.getChldList() != null) {
				sno = recursion(item.getChldList(), 0, sno, sno - 1);
			}
		}
		return sno;
	}

	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CreateItem {

		@JsonIgnore
		@Schema(description = "일련번호", example = "123456")
		private Long sno;

		@JsonIgnore
		@Schema(description = "부모메뉴번호", example = "123456")
		private Long prntMenuNo;

		@NotEmpty
		@Schema(description = "메뉴명", example = "사용자 관리")
		private String nm;

		@Schema(description = "메뉴설명", example = "사용자를 관리합니다.")
		private String expl;

		@Schema(description = "메뉴URL", example = "/adm/user")
		private String url;

		@Schema(description = "역할 목록")
		private Role[] roleList;

		@Schema(description = "하위 메뉴")
		private CreateItem[] chldList;
	}
}