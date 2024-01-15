package artxew.framework.layers.dmn.svc;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.dmn.dto.req.CreateList_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.req.ModifyHgNm_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.req.QueryList_Dmn_ReqDto;
import artxew.framework.layers.dmn.dto.res.QueryList_Dmn_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class DmnSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	public int createList_Dmn(CreateList_Dmn_ReqDto reqDto) {
		int result = commonDao.insert(reqDto);
		if (result < 1) {
			exception("conflict");
		}
		return result;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int modifyHgNm_Dmn(
		ModifyHgNm_Dmn_ReqDto reqDto
	) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	public List<QueryList_Dmn_ResDto> queryList_Dmn(QueryList_Dmn_ReqDto reqDto) {
		return commonDao.selectList(reqDto);
	}
}