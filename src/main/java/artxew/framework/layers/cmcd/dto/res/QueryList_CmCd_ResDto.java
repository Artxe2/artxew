package artxew.framework.layers.cmcd.dto.res;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class QueryList_CmCd_ResDto {
	private QueryList_CmCd_ResDto() {
		throw new IllegalStateException("Dto namespace class");
	}
	/**
	 * @author Artxe2
	 */
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QueryItem implements Serializable {

		@NotNull
		@Schema(description = "그룹코드", example = "ROLE_CD")
		private String grpCd;

		@NotNull
		@Schema(description = "코드", example = "01")
		private String cd;

		@Schema(description = "이름", example = "최고관리자")
		private String nm;
	}
}
