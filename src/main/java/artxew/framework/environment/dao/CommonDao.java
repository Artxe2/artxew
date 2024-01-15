package artxew.framework.environment.dao;
import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import artxew.framework.decedent.dto.PageReqDto;
import artxew.framework.environment.exception.DefinedException;

/**
 * @author Artxe2
 */
@Component
public final class CommonDao {

	@Autowired @Qualifier("sqlSessionCommand")
	private SqlSessionTemplate sqlSessionCommand;

	@Autowired @Qualifier("sqlSessionQuery")
	private SqlSessionTemplate sqlSessionQuery;

	/**
	 * @author Artxe2
	 */
	public <T> T select() {
		return sqlSessionQuery.selectOne(statement());
	}

	/**
	 * @author Artxe2
	 */
	public <T> T select(Object dto) {
		return sqlSessionQuery.selectOne(statement(), dto);
	}

	/**
	 * @author Artxe2
	 */
	public <T> T selectUsingCommand(Object dto) {
		return sqlSessionCommand.selectOne(statement(), dto);
	}

	/**
	 * @author Artxe2
	 */
	public <E> List<E> selectList() {
		return sqlSessionQuery.selectList(statement());
	}

	/**
	 * @author Artxe2
	 */
	public <E> List<E> selectList(Object dto) {
		return sqlSessionQuery.selectList(statement(), dto);
	}

	/**
	 * @author Artxe2
	 */
	public <E> List<E> selectPage(PageReqDto dto) {
		int limit = dto.getSize();
		int offset = (dto.getPage() - 1) * limit;
		return sqlSessionQuery.selectList(statement(), dto, new RowBounds(offset, limit));
	}

	/**
	 * @author Artxe2
	 */
	public int insert() {
		try {
			return sqlSessionCommand.insert(statement());
		} catch (DuplicateKeyException | MyBatisSystemException e) {
			throw new DefinedException("conflict", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public int insert(Object dto) {
		try {
			return sqlSessionCommand.insert(statement(), dto);
		} catch (DuplicateKeyException | MyBatisSystemException e) {
			throw new DefinedException("conflict", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public int update(Object dto) {
		try {
			return sqlSessionCommand.update(statement(), dto);
		} catch (DuplicateKeyException | MyBatisSystemException e) {
			throw new DefinedException("conflict", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public int delete(Object dto) {
		try {
			return sqlSessionCommand.delete(statement(), dto);
		} catch (DuplicateKeyException | MyBatisSystemException e) {
			throw new DefinedException("conflict", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public void procedure() {
		sqlSessionCommand.update(statement());
	}

	/**
	 * @author Artxe2
	 */
	public void procedure(Object dto) {
		sqlSessionCommand.update(statement(), dto);
	}

	/**
	 * @author Artxe2
	 */
	private String statement() {
		StackTraceElement context = Thread.currentThread().getStackTrace()[3];
		String className = context.getClassName();
		return className.substring(0, className.lastIndexOf(".svc") + 1) + context.getMethodName();
	}
}