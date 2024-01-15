package artxew.framework.decedent.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class PageReqDto {

	@Schema(description = "현재 페이지", example = "1")
	private int page = 1;

	@Schema(description = "페이지당 콘텐츠 수", example = "25")
	private int size = 25;
}