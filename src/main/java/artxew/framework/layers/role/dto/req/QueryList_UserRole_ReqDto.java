package artxew.framework.layers.role.dto.req;
import artxew.framework.decedent.dto.PageReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class QueryList_UserRole_ReqDto extends PageReqDto {

	@Schema(description = "사용자ID", example = "test1234")
	private String userId;

	@Schema(description = "권한코드", example = "02")
	private String roleCd;
}