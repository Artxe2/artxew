package artxew.project.layers.test.svc;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@Profile({ "dev", "local", "stg" })
@RequiredArgsConstructor
@Service
public class TestSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	public String currentTime() {
		return commonDao.select();
	}

	/**
	 * @author Artxe2
	 */
	public List<Map<String, Object>> query() {
		return commonDao.selectList();
	}
}