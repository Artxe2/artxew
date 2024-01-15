package artxew.framework.layers.cmcd.ctrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.layers.cmcd.dto.res.QueryList_CmCd_ResDto;
import artxew.framework.layers.cmcd.svc.CmCdSvc;
import artxew.project.enums.StringYN;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Artxe2
 */
@RequestMapping("api/cm-cd")
@RequiredArgsConstructor
@RestController
@Tag(name = "공통 코드 관리")
public class CmCdCtrl extends BaseController {
	private final CmCdSvc cmCdSvc;
	private final CacheManager cacheManager;

	/**
	 * @author Artxe2
	 */
	@GetMapping("{cmCd}")
	@Operation(summary = "공통 코드 조회")
	public ResponseEntity<ServerResponseDto<
	QueryList_CmCd_ResDto.QueryItem[]
	>> queryList_CmCd(
		@Parameter(name = "cmCd", example = "ACC_CL_CD") @PathVariable("cmCd")
		String cmCd
		, @RequestParam(defaultValue = "N", name = "refreshYn", required = false)
		StringYN refreshYn
	) {
		if (StringYN.Y == refreshYn) {
			Cache cache = cacheManager.getCache("queryList_CmCd");
			if (cache != null) {
				cache.clear();
			}
		}
		return processResult(
			cmCd == null
				? cmCdSvc.queryList_CmCd()
				: cmCdSvc.queryList_CmCd(cmCd)
		);
	}
}