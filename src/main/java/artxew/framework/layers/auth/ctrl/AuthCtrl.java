package artxew.framework.layers.auth.ctrl;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
// import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
// import org.springframework.session.Session;
// import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.AuthCheck;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.layers.auth.dto.req.SelfCert_ReqDto;
import artxew.framework.layers.auth.dto.req.Modify_User_ReqDto;
import artxew.framework.layers.auth.dto.req.ResetPwd_ReqDto;
import artxew.framework.layers.auth.dto.req.SetTempPwd_User_ReqDto;
import artxew.framework.layers.auth.dto.req.SignIn_User_ReqDto;
import artxew.framework.layers.auth.dto.req.SignUp_User_ReqDto;
import artxew.framework.layers.auth.dto.req.Withdrawal_User_ReqDto;
import artxew.framework.layers.auth.dto.res.SignIn_User_ResDto;
import artxew.framework.layers.auth.dto.res.QueryIdExists_User_ResDto;
import artxew.framework.layers.auth.dto.res.SignUp_User_ResDto;
import artxew.framework.layers.auth.svc.AuthSvc;
import artxew.framework.util.SessionHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Artxe2
 */
@RequestMapping("api")
@RequiredArgsConstructor
@RestController
@Tag(name = "계정 인증 관리")
public class AuthCtrl extends BaseController {
	private final AuthSvc authSvc;
	// private final RedisTemplate<String, Object> redisTemplate;
	// private final SessionRepository<?> sessionRepository;
	private final Pattern idRegex = Pattern.compile("^[\\dA-Za-z]{3,20}$");
	private final Pattern pwdRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^\\dA-Za-z]).{8,20}$");

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "사용자 차단"
	)
	@PatchMapping("auth/{userNo}/blck")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void block_User(
		@PathVariable("userNo")
		long userNo
	) {
		authSvc.block_User(userNo);
		// String pattern = "artxew:sessions:*";
		// int index = pattern.length() - 1;
		// for (var key : redisTemplate.keys(pattern)) {
		// 	Session session = sessionRepository.findById(key.substring(index));
		// 	if (session == null) continue;
		// 	Long sno = session.getAttribute(SessionHelper.USER_NO);
		// 	if (sno == null) continue;
		// 	if (userNo == sno) {
		// 		redisTemplate.delete(key);
		// 	}
		// }
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "본인인증")
	@PostMapping("hook/self-cert")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void selfCert(
		@NonNull @RequestBody @Valid
		SelfCert_ReqDto reqDto
	) throws IOException {
		SessionHelper.put("SelfCert_ReqDto", reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "사용자 정보 수정")
	@PutMapping("auth")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modify_User(
		@RequestBody @Valid
		Modify_User_ReqDto reqDto
	) {
		String pwd = reqDto.getPwd();
		if (pwd != null && !pwd.isEmpty() && !pwdRegex.matcher(pwd).find()) {
			exception("invalid-pwd-error");
		}
		SelfCert_ReqDto selfCert = (SelfCert_ReqDto) SessionHelper.get("SelfCert_ReqDto");
		if (selfCert != null) {
			BeanUtils.copyProperties(selfCert, reqDto);
		}
		authSvc.modify_User(reqDto);
		SessionHelper.remove("SelfCert_ReqDto");
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "세션 정보 조회")
	@GetMapping("auth")
	public ResponseEntity<ServerResponseDto<
	SignIn_User_ResDto
	>> queryAuthInfo() {
		SignIn_User_ResDto resDto = new SignIn_User_ResDto();
		resDto.setSno(SessionHelper.sno());
		resDto.setId(SessionHelper.id());
		resDto.setRoles(SessionHelper.roles());
		resDto.setNm(SessionHelper.nm());
		resDto.setTel((String) SessionHelper.get("tel"));
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "아이디 중복 체크")
	@GetMapping("auth/id/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<ServerResponseDto<
	QueryIdExists_User_ResDto
	>> queryIdExists_User(
		@PathVariable("userId")
		String userId
	) {
		QueryIdExists_User_ResDto resDto = authSvc.queryIdExists_User(userId);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "본인인증 아이디 목록 조회")
	@GetMapping("auth/id")
	public ResponseEntity<ServerResponseDto<
	List<String>
	>> queryIdList_User() {
		SelfCert_ReqDto selfCert = (SelfCert_ReqDto) SessionHelper.get("SelfCert_ReqDto");
		if (selfCert == null) {
			throw exception("no-self-certification-error");
		}
		List<String> resDto = authSvc.queryIdList_User(selfCert.getCi());
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "비밀번호 재설정")
	@PatchMapping("auth/{userNo}/rset-pwd")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resetPwd_User(
		@PathVariable("userNo")
		long userNo
		, @RequestBody @Valid
		ResetPwd_ReqDto reqDto
	) {
		if (!pwdRegex.matcher(reqDto.getPwd()).find()) {
			exception("invalid-pwd-error");
		}
		reqDto.setSno(userNo);
		authSvc.resetPwd_User(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "비밀번호 오류 횟수 초기화"
	)
	@PatchMapping("auth/{userNo}/rset-err-cnt")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resetPwdErrCnt_User(
		@PathVariable("userNo")
		long userNo
	) {
		authSvc.resetPwdErrCnt_User(userNo);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "비밀번호 재설정 메일 발송")
	@PatchMapping("auth/temp-pwd")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void setTempPwd(
		@RequestBody @Valid
		SetTempPwd_User_ReqDto reqDto
	) {
		SelfCert_ReqDto selfCert = (SelfCert_ReqDto) SessionHelper.get("SelfCert_ReqDto");
		if (selfCert == null) {
			throw exception("no-self-certification-error");
		}
		reqDto.setCi(selfCert.getCi());
		authSvc.setTempPwd_User(reqDto);
		SessionHelper.remove("SelfCert_ReqDto");
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "로그인")
	@PostMapping("auth/in")
	@SuppressWarnings("null")
	public ResponseEntity<ServerResponseDto<
	SignIn_User_ResDto
	>> signIn_User(@RequestBody @Valid SignIn_User_ReqDto reqDto) {
		signOut();
		SignIn_User_ResDto resDto = authSvc.signIn_User(reqDto);
		SessionHelper.put(SessionHelper.USER_NO, resDto.getSno());
		SessionHelper.put(SessionHelper.USER_ID, resDto.getId());
		SessionHelper.put(SessionHelper.USER_ROLES, resDto.getRoles());
		SessionHelper.put(SessionHelper.USER_NM, resDto.getNm());
		SessionHelper.put("tel", resDto.getTel());
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "회원가입")
	@PostMapping("auth/up")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ServerResponseDto<
	SignUp_User_ResDto
	>> signUp_User(
		@RequestBody @Valid
		SignUp_User_ReqDto reqDto
	) {
		if (!idRegex.matcher(reqDto.getId()).find()) {
			exception("invalid-id-error");
		}
		if (!pwdRegex.matcher(reqDto.getPwd()).find()) {
			exception("invalid-pwd-error");
		}
		SelfCert_ReqDto selfCert = (SelfCert_ReqDto) SessionHelper.get("SelfCert_ReqDto");
		if (selfCert == null) {
			throw exception("no-self-certification-error");
		}
		BeanUtils.copyProperties(selfCert, reqDto);
		authSvc.signUp_User(reqDto);
		SessionHelper.remove("SelfCert_ReqDto");
		SignUp_User_ResDto resDto = new SignUp_User_ResDto();
		resDto.setSno(reqDto.getSno());
		resDto.setId(reqDto.getId());
		return processResult(resDto, 201);
	}

	/**
	 * @author Artxe2
	 */
	@DeleteMapping("auth/out")
	@Operation(summary = "로그아웃")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void signOut() {
		HttpSession session = SessionHelper.session(false);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "사용자 차단 해제"
	)
	@PatchMapping("auth/{userNo}/ublk")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unblock_User(
		@PathVariable("userNo")
		long userNo
	) {
		authSvc.unblock_User(userNo);
	}

	/**
	 * @author Artxe2
	 */
	@DeleteMapping("auth/wdrw")
	@Operation(summary = "회원탈퇴")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void withdrawal_User(
		@RequestBody @Valid
		Withdrawal_User_ReqDto reqDto
	) {
		SelfCert_ReqDto selfCert = (SelfCert_ReqDto) SessionHelper.get("SelfCert_ReqDto");
		if (selfCert == null) {
			throw exception("no-self-certification-error");
		}
		reqDto.setCi(selfCert.getCi());
		authSvc.withdrawal_User(reqDto);
		HttpSession session = SessionHelper.session(false);
		if (session != null) {
			session.invalidate();
		}
	}
}