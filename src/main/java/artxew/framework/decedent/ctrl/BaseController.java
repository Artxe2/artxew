package artxew.framework.decedent.ctrl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.exception.DefinedException;

/**
 * @author Artxe2
 */
public class BaseController {

	/**
	 * @author Artxe2
	 */
	public <T> ResponseEntity<ServerResponseDto<T>> processResult(T data) {
		return new ResponseEntity<>(new ServerResponseDto<>(data), HttpStatus.OK);
	}

	/**
	 * @author Artxe2
	 */
	public <T> ResponseEntity<ServerResponseDto<T>> processResult(T data, int status) {
		return ResponseEntity.status(status).body(new ServerResponseDto<>(data));
	}

	/**
	 * @author Artxe2
	 */
	public <T> ResponseEntity<ServerResponseDto<T>> processResult(T data, String message) {
		return new ResponseEntity<>(new ServerResponseDto<>(data, message), HttpStatus.OK);
	}

	/**
	 * @author Artxe2
	 */
	public <T> ResponseEntity<ServerResponseDto<T>> processResult(T data, String message, int status) {
		return ResponseEntity.status(status).body(new ServerResponseDto<>(data, message));
	}

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