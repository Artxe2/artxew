package artxew.framework.layers.role.svc;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.role.dto.req.Create_UserRole_ReqDto;
import artxew.framework.layers.role.dto.req.QueryList_UserRole_ReqDto;
import artxew.framework.layers.role.dto.res.QueryList_Role_ResDto;
import artxew.framework.layers.role.dto.res.QueryList_UserRole_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class RoleSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int create_UserRole(Create_UserRole_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int delete_UserRole(Create_UserRole_ReqDto reqDto) {
		if (commonDao.delete(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	public List<QueryList_Role_ResDto.QueryItem> queryList_Role() {
		return commonDao.selectList();
	}

	/**
	 * @author Artxe2
	 */
	private int queryListCnt_UserRole(QueryList_UserRole_ReqDto reqDto) {
		return commonDao.select(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	public QueryList_UserRole_ResDto queryList_UserRole(QueryList_UserRole_ReqDto reqDto) {
		QueryList_UserRole_ResDto resDto = new QueryList_UserRole_ResDto();
		int count = queryListCnt_UserRole(reqDto);
		List<QueryList_UserRole_ResDto.QueryItem> dataList = commonDao.selectPage(reqDto);
		resDto.setSize(reqDto.getSize());
		resDto.setPage(reqDto.getPage());
		resDto.setCount(count);
		resDto.setDataList(dataList);
		List<QueryList_Role_ResDto.QueryItem> roleList = queryList_Role();
		for (var user : dataList) {
			if (user.getRolesStr() != null) {
				for (var cd : user.getRolesStr().split(",")) {
					for (var role : roleList) {
						if (cd.equals(role.getCd())) {
							user.getRoleList().add(role);
							break;
						}
					}
				}
			}
		}
		return resDto;
	}
}