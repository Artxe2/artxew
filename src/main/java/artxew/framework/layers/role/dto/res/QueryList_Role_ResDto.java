package artxew.framework.layers.role.dto.res;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
public class QueryList_Role_ResDto {
	private QueryList_Role_ResDto() {
		throw new IllegalStateException("Dto namespace class");
	}
	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem {

		@NotEmpty
		@Schema(description = "코드", example = "02")
		private String cd;

		@Schema(description = "이름", example = "ADMIN 관리자")
		private String nm;
	}
}