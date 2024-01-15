package artxew.framework.layers.auth.svc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class AuthSvcFinally extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int incrementPwdErrCnt_User(long sno) {
		if (commonDao.update(sno) != 1) {
			exception("conflict");
		}
		return 1;
	}
}