package artxew.framework.layers.cmcd.svc;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.cmcd.dto.res.QueryList_CmCd_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class CmCdSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	private QueryList_CmCd_ResDto.QueryItem[] filter_Cmcd(QueryList_CmCd_ResDto.QueryItem[] array, String cdGrp) {
		int start = findStart(array, cdGrp);
		if (start == array.length) {
			return new QueryList_CmCd_ResDto.QueryItem[0];
		}
		int end = findEnd(array, cdGrp, start);
		int length = end - start;
		QueryList_CmCd_ResDto.QueryItem[] result = new QueryList_CmCd_ResDto.QueryItem[length];
		System.arraycopy(array, start, result, 0, length);
		return result;
	}

	/**
	 * @author Artxe2
	 */
	private int findEnd(QueryList_CmCd_ResDto.QueryItem[] array, String cdGrp, int start) {
		int mid;
		int end = array.length;
		String temp;
		while (start < end) {
			mid = (start + end - 1) / 2;
			temp = array[mid].getGrpCd();
			if (cdGrp.compareTo(temp) < 0) {
				end = mid;
			} else {
				start = mid + 1;
			}
		}
		return start;
	}

	/**
	 * @author Artxe2
	 */
	private int findStart(QueryList_CmCd_ResDto.QueryItem[] array, String cdGrp) {
		int length = array.length;
		int start = 0;
		int mid;
		int end = length;
		String temp;
		while (start < end) {
			mid = (start + end - 1) / 2;
			temp = array[mid].getGrpCd();
			if (cdGrp.compareTo(temp) <= 0) {
				end = mid;
			} else {
				start = mid + 1;
			}
		}
		if (end == length) {
			return length;
		}
		temp = array[end].getGrpCd();
		if (!cdGrp.equals(temp)) {
			return length;
		}
		return end;
	}

	/**
	 * @author Artxe2
	 */
	@Cacheable("queryList_CmCd")
	public QueryList_CmCd_ResDto.QueryItem[] queryList_CmCd() {
		List<QueryList_CmCd_ResDto.QueryItem> list = commonDao.selectList();
		QueryList_CmCd_ResDto.QueryItem[] array = new QueryList_CmCd_ResDto.QueryItem[list.size()];
		return list.toArray(array);
	}

	/**
	 * @author Artxe2
	 */
	@Cacheable(value = "queryList_CmCd", key = "#a0")
	public QueryList_CmCd_ResDto.QueryItem[] queryList_CmCd(String cdGrp) {
		return filter_Cmcd(queryList_CmCd(), cdGrp);
	}

	/**
	 * @author Artxe2
	 */
	@Cacheable(value = "queryList_CmCd", key = "#a1")
	public QueryList_CmCd_ResDto.QueryItem[] queryList_CmCd(QueryList_CmCd_ResDto.QueryItem[] array, String cdGrp) {
		return filter_Cmcd(array, cdGrp);
	}
}