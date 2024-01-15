package artxew.framework.decedent.dto;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResDto<T> {

	@NotNull
	@Schema(description = "현재 페이지", example = "1")
	private Integer page;

	@NotNull
	@Schema(description = "페이지당 콘텐츠 수", example = "25")
	private Integer size;

	@Schema(description = "전체 콘텐츠 수", example = "1234")
	private Integer count;

	@NotNull
	@Schema(description = "데이터 목록")
	private List<T> dataList;
}