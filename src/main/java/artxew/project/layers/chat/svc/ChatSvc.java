package artxew.project.layers.chat.svc;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.project.layers.chat.dto.req.Create_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Delete_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.req.MarkRead_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.MarkWrite_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.QueryList_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.req.QueryList_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Query_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Create_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.res.Query_Chat_ResDto;
import artxew.project.layers.chat.dto.res.QueryList_ChatBlck_ResDto;
import artxew.project.layers.chat.dto.res.QueryList_Chat_ResDto;
import artxew.project.layers.chat.dto.res.QueryNewList_Chat_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class ChatSvc extends BaseService {
	private final CommonDao commonDao;

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int create_Chat(Create_Chat_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		createList_ChatJoin(reqDto);
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int create_ChatBlck(Create_ChatBlck_ReqDto reqDto) {
		if (commonDao.insert(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int createList_ChatJoin(Create_Chat_ReqDto reqDto) {
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
	public int delete_ChatBlck(Delete_ChatBlck_ReqDto reqDto) {
		if (commonDao.delete(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int markRead_Chat(MarkRead_Chat_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int markWrite_Chat(MarkWrite_Chat_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 2) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	private int queryListCnt_Chat(QueryList_Chat_ReqDto reqDto) {
		return commonDao.select(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	private int queryListCnt_ChatBlck(QueryList_ChatBlck_ReqDto reqDto) {
		return commonDao.select(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	public QueryList_Chat_ResDto queryList_Chat(QueryList_Chat_ReqDto reqDto) {
		QueryList_Chat_ResDto resDto = new QueryList_Chat_ResDto();
		int count = queryListCnt_Chat(reqDto);
		List<QueryList_Chat_ResDto.QueryItem> dataList = commonDao.selectPage(reqDto);
		resDto.setSize(reqDto.getSize());
		resDto.setPage(reqDto.getPage());
		resDto.setCount(count);
		resDto.setDataList(dataList);
		return resDto;
	}

	/**
	 * @author Artxe2
	 */
	public QueryList_ChatBlck_ResDto queryList_ChatBlck(QueryList_ChatBlck_ReqDto reqDto) {
		QueryList_ChatBlck_ResDto resDto = new QueryList_ChatBlck_ResDto();
		int count = queryListCnt_ChatBlck(reqDto);
		List<QueryList_ChatBlck_ResDto.QueryItem> dataList = commonDao.selectPage(reqDto);
		resDto.setSize(reqDto.getSize());
		resDto.setPage(reqDto.getPage());
		resDto.setCount(count);
		resDto.setDataList(dataList);
		return resDto;
	}

	/**
	 * @author Artxe2
	 */
	private List<Query_Chat_ResDto.JoinUser> queryList_ChatJoin(long chatNo) {
		return commonDao.selectList(chatNo);
	}

	/**
	 * @author Artxe2
	 */
	public List<QueryNewList_Chat_ResDto> queryNewList_Chat(List<Long> reqDto) {
		return commonDao.selectList(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	public Query_Chat_ResDto query_Chat(Query_Chat_ReqDto reqDto) {
		Query_Chat_ResDto resDto = commonDao.select(reqDto);
		if (resDto == null) {
			throw exception("not-found");
		}
		List<Query_Chat_ResDto.JoinUser> joinList = queryList_ChatJoin(reqDto.getChatNo());
		resDto.setJoinList(joinList);
		return resDto;
	}
}