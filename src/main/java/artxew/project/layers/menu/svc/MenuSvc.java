package artxew.project.layers.menu.svc;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.project.layers.menu.dto.req.ReplaceAll_Menu_ReqDto;
import artxew.project.layers.menu.dto.res.QueryList_Menu_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class MenuSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	private int deleteAll_Menu() {
		int result = commonDao.delete(null);
		if (result < 0) {
			exception("conflict");
		}
		return result;
	}

	/**
	 * @author Artxe2
	 */
	private int deleteAll_MenuRole() {
		int result = commonDao.delete(null);
		if (result < 0) {
			exception("conflict");
		}
		return result;
	}

	/**
	 * @author Artxe2
	 */
	@Cacheable(value = "queryList_Menu", key = "#a0")
	public List<QueryList_Menu_ResDto.QueryItem> queryList_Menu(String roles) {
		String[] reqDto = roles.split(",");
		List<QueryList_Menu_ResDto.QueryItem> resDto = commonDao.selectList(reqDto);
		int length = resDto.size();
		int index = length;
		while (index-- > 0) {
			QueryList_Menu_ResDto.QueryItem item = resDto.get(index);
			long sno = item.getSno();
			int i = index;
			while (i < length) {
				Long prntNo = resDto.get(i).getPrntMenuNo();
				if (prntNo != null && sno == prntNo) {
					if (item.getChldList() == null) {
						item.setChldList(new ArrayList<>());
					}
					item.getChldList().add(resDto.remove(i));
					length--;
				} else {
					i++;
				}
			}
		}
		return resDto;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int replaceAll_Menu(ReplaceAll_Menu_ReqDto reqDto) {
		deleteAll_MenuRole();
		deleteAll_Menu();
		if (commonDao.insert(reqDto) != reqDto.getData().size()) {
			exception("conflict");
		}
		for (var item : reqDto.getData()) {
			if (item.getRoleList() != null && item.getRoleList().length > 0) {
				replaceAll_MenuRole(reqDto);
				break;
			}
		}
		return reqDto.getData().size();
	}

	/**
	 * @author Artxe2
	 */
	private int replaceAll_MenuRole(ReplaceAll_Menu_ReqDto reqDto) {
		int result = commonDao.insert(reqDto);
		if (result < 0) {
			exception("conflict");
		}
		return result;
	}
}