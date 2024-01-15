package artxew.project.layers.menu.ctrl;
import java.util.List;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.AuthCheck;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.util.SessionHelper;
import artxew.project.enums.StringYN;
import artxew.project.layers.menu.dto.req.ReplaceAll_Menu_ReqDto;
import artxew.project.layers.menu.dto.res.QueryList_Menu_ResDto;
import artxew.project.layers.menu.svc.MenuSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequestMapping("api/menu")
@RequiredArgsConstructor
@RestController
@Tag(name = "메뉴 관리")
public class MenuCtrl extends BaseController {
	private final CacheManager cacheManager;
	private final MenuSvc menuSvc;

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@GetMapping
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "메뉴 목록 조회"
	)
	public ResponseEntity<ServerResponseDto<
	List<QueryList_Menu_ResDto.QueryItem>
	>> queryList_Menu(
		@RequestParam(defaultValue = "N", name = "refreshYn", required = false)
		StringYN refreshYn
	) {
		if (StringYN.Y == refreshYn) {
			Cache cache = cacheManager.getCache("queryList_Menu");
			if (cache != null) {
				cache.clear();
			}
		}
		Role[] roles = SessionHelper.roles();
		StringBuilder sb = new StringBuilder();
		for (var r : roles) {
			sb.append(r.getCode()).append(",");
		}
		sb.setLength(sb.length() - 1);
		List<QueryList_Menu_ResDto.QueryItem> resDto = menuSvc.queryList_Menu(sb.toString());
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck(Role.SUPER)
	@Operation(
		description = "@AuthCheck(Role.SUPER)"
		, summary = "메뉴 목록 저장"
	)
	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void replaceAll_Menu(
		@RequestBody @Valid
		ReplaceAll_Menu_ReqDto reqDto
	) {
		menuSvc.replaceAll_Menu(reqDto);
		Cache cache = cacheManager.getCache("queryList_Menu");
		if (cache != null) {
			cache.clear();
		}
	}
}