package artxew.framework.layers.auth.svc;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.auth.dto.req.SignUp_User_ReqDto;
import artxew.framework.layers.auth.dto.req.Withdrawal_User_ReqDto;
import artxew.framework.layers.auth.dto.res.SignIn_User_ResDto;
import artxew.framework.layers.auth.dto.res.QueryIdExists_User_ResDto;
import artxew.framework.layers.auth.dto.req.Modify_User_ReqDto;
import artxew.framework.layers.auth.dto.req.ResetPwd_ReqDto;
import artxew.framework.layers.auth.dto.req.SetTempPwd_User_ReqDto;
import artxew.framework.layers.auth.dto.req.SignIn_User_ReqDto;
import artxew.framework.util.MailSender;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class AuthSvc extends BaseService {

	@Value("${artxew.domain}")
	private String domain;

	private final CommonDao commonDao;
	private final AuthSvcFinally authSvcFinally;

	/**
	 * @author Artxe2
	 */
	public int block_User(long userNo) {
		if (commonDao.update(userNo) < 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	public void checkBlocked_User(long userNo) {
		int result = commonDao.select(userNo);
		if (result != 0) {
			exception("blocked-user-error");
		}
	}

	/**
	 * @author Artxe2
	 */
	private void checkExists_User(String ci) {
		int result = commonDao.select(ci);
		if (result != 0) {
			exception("exist-rejoin-error");
		}
	}

	/**
	 * @author Artxe2
	 */
	private void check_PwdChgHst(long userNo, String pwd) {
		List<String> list = commonDao.selectList(userNo);
		for (var item : list) {
			String[] tokens = item.split(":");
			String hash = StringUtil.pbkdf2(pwd, tokens[0]);
			if (tokens[1].equals(hash)) {
				exception("already-pwd-error");
			}
		}
	}

	/**
	 * @author Artxe2
	 */
	private void checkWithdrawal_User(String ci) {
		int result = commonDao.select(ci);
		if (result != 0) {
			exception("withdrawn-rejoin-error");
		}
	}

	/**
	 * @author Artxe2
	 */
	private int create_LginHst(SignIn_User_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	private int create_PwdChgHst(Modify_User_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}
	private int create_PwdChgHst(ResetPwd_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}
	private int create_PwdChgHst(SignUp_User_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int modify_User(Modify_User_ReqDto reqDto) {
		String pwd = reqDto.getPwd();
		if (pwd != null && !pwd.isEmpty()) {
			check_PwdChgHst(reqDto.getSno(), pwd);
			reqDto.setPwd(StringUtil.hashingPassword(pwd));
			create_PwdChgHst(reqDto);
		}
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	public QueryIdExists_User_ResDto queryIdExists_User(String userId) {
		return commonDao.select(userId);
	}

	/**
	 * @author Artxe2
	 */
	public List<String> queryIdList_User(String ci) {
		return commonDao.selectList(ci);
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int resetPwd_User(ResetPwd_ReqDto reqDto) {
		String pwd = reqDto.getPwd();
		check_PwdChgHst(reqDto.getSno(), pwd);
		reqDto.setPwd(StringUtil.hashingPassword(pwd));
		create_PwdChgHst(reqDto);
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int resetPwdErrCnt_User(long userNo) {
		if (commonDao.update(userNo) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int setTempPwd_User(SetTempPwd_User_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		MailSender.send(
			"[Artxew] 비밀번호 재설정"
			, new StringBuilder("<a href=\"")
				.append(domain)
				.append("/swagger-ui/index.html?urls.primaryName=Framework#/계정 인증 관리/resetPwd_User")
				.append("\">비밀번호 재설정</a><br>")
				.append("<b>userNo: </b>")
				.append(reqDto.getSno())
				.append("<br><b>tempPwd: </b>")
				.append(reqDto.getTempPwd())
				.toString()
			, reqDto.getMail()
		);
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public SignIn_User_ResDto signIn_User(SignIn_User_ReqDto reqDto) {
		SignIn_User_ResDto resDto = commonDao.select(reqDto.getId());
		if (resDto == null) {
			throw exception("sign-in-fail-error");
		}
		if (resDto.getErr() >= 5) {
			exception("pwd-error-exceeded");
		}
		// if (resDto.getBlck()) {
		// 	exception("blocked-user-error");
		// }
		reqDto.setSno(resDto.getSno());
		String[] tokens = resDto.getPwd().split(":");
		String hash = StringUtil.pbkdf2(reqDto.getPwd(), tokens[0]);
		if (!tokens[1].equals(hash)) {
			authSvcFinally.incrementPwdErrCnt_User(reqDto.getSno());
			throw exception("sign-in-fail-error");
		}
		reqDto.setIp(SessionHelper.ip());
		resetPwdErrCnt_User(reqDto.getSno());
		create_LginHst(reqDto);
		return resDto;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int signUp_User(SignUp_User_ReqDto reqDto) {
		checkExists_User(reqDto.getCi());
		checkWithdrawal_User(reqDto.getCi());
		reqDto.setPwd(StringUtil.hashingPassword(reqDto.getPwd()));
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		create_PwdChgHst(reqDto);
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int unblock_User(long userNo) {
		if (commonDao.update(userNo) < 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int withdrawal_User(Withdrawal_User_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}
}