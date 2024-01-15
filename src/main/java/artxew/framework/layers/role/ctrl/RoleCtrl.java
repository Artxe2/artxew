package artxew.framework.layers.role.ctrl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.AuthCheck;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.layers.role.dto.req.Create_UserRole_ReqDto;
import artxew.framework.layers.role.dto.req.QueryList_UserRole_ReqDto;
import artxew.framework.layers.role.dto.res.QueryList_Role_ResDto;
import artxew.framework.layers.role.dto.res.QueryList_UserRole_ResDto;
import artxew.framework.layers.role.svc.RoleSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequestMapping("api")
@RequiredArgsConstructor
@RestController
@Tag(name = "역할 관리")
public class RoleCtrl extends BaseController {
	private final RoleSvc roleSvc;

	/**
	 * @author Artxe2
	 */
	@AuthCheck(Role.SUPER)
	@Operation(
		description = "@AuthCheck(Role.SUPER)"
		, summary = "회원 역할 부여"
	)
	@PostMapping("user/{userNo}/role")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create_UserRole(
		@PathVariable("userNo")
		long userNo
		, @RequestBody @Valid
		Create_UserRole_ReqDto reqDto
	) {
		reqDto.setUserNo(userNo);
		roleSvc.create_UserRole(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck(Role.SUPER)
	@DeleteMapping("user/{userNo}/role")
	@Operation(
		description = "@AuthCheck(Role.SUPER)"
		, summary = "회원 역할 회수"
	)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete_UserRole(
		@PathVariable("userNo")
		long userNo
		, @RequestBody @Valid
		Create_UserRole_ReqDto reqDto
	) {
		reqDto.setUserNo(userNo);
		roleSvc.delete_UserRole(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck(Role.SUPER)
	@GetMapping("role")
	@Operation(
		description = "@AuthCheck(Role.SUPER)"
		, summary = "역할 목록 조회"
	)
	public ResponseEntity<ServerResponseDto<
	List<QueryList_Role_ResDto.QueryItem>
	>> queryList_Role() {
		List<QueryList_Role_ResDto.QueryItem> resDto = roleSvc.queryList_Role();
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck(Role.SUPER)
	@GetMapping("user-role")
	@Operation(
		description = "@AuthCheck(Role.SUPER)"
		, summary = "회원 역할 목록 조회"
	)
	public ResponseEntity<ServerResponseDto<
	QueryList_UserRole_ResDto
	>> queryList_UserRole(
		@Parameter(name = "queries") @Valid
		QueryList_UserRole_ReqDto reqDto
	) {
		QueryList_UserRole_ResDto resDto = roleSvc.queryList_UserRole(reqDto);
		return processResult(resDto);
	}
}