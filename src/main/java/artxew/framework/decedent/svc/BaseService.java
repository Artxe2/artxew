package artxew.framework.decedent.svc;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.exception.DefinedException;

/**
 * @author Artxe2
 */
public class BaseService {

	/**
	 * @author Artxe2
	 */
	public DefinedException exception(Object data, String key) {
		throw new DefinedException(new ServerResponseDto<>(data), key, null);
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException exception(String key) {
		throw new DefinedException(null, key, null);
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException exception(Object data, String key, Throwable e) {
		throw new DefinedException(new ServerResponseDto<>(data), key, e);
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException exception(String key, Throwable e) {
		throw new DefinedException(null, key, e);
	}
}