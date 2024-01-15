package artxew.framework.layers.dmn.ctrl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.layers.dmn.dto.req.CreateList_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.req.ModifyHgNm_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.req.QueryList_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.res.QueryList_Dmn_ResDto;
import artxew.framework.layers.dmn.svc.DmnSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Artxe2
 */
@RequestMapping("api/dmn")
@RequiredArgsConstructor
@RestController
@Tag(name = "도메인 용어 관리")
public class DmnCtrl extends BaseController {
	private final DmnSvc dmnSvc;

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "도메인 용어 등록")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ServerResponseDto<
	Integer
	>> createList_Dmn(
		@RequestBody
		CreateList_Dmn_ReqDto reqDto
	) {
		int result = dmnSvc.createList_Dmn(reqDto);
		return processResult(result, 201);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "도메인 용어 수정")
	@PostMapping("{abr}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyHgNm_Dmn(
		@PathVariable("abr")
		String abr
		, @RequestBody
		ModifyHgNm_Dmn_ReqDto reqDto
	) {
		reqDto.setAbr(abr);
		dmnSvc.modifyHgNm_Dmn(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "도메인 용어 조회")
	@GetMapping
	public ResponseEntity<ServerResponseDto<
	List<QueryList_Dmn_ResDto>
	>> queryList_Dmn(
		@Parameter(name = "queries") @Valid
		QueryList_Dmn_ReqDto reqDto
	) {
		List<QueryList_Dmn_ResDto> resDto = dmnSvc.queryList_Dmn(reqDto);
		return processResult(resDto);
	}
}