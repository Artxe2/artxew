package artxew.framework.environment.exception;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.flowlog.FlowLogHolder;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.SpringContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@RestControllerAdvice
@Slf4j
public final class ExceptionResponseHandler {

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(BindException.class)
	private ResponseEntity<ServerResponseDto<Object>> badRequestHandler(
		BindException e
	) {
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.badRequestHandler(")
			.append(e.getBindingResult().getErrorCount())
			.append(" errors)");
		FlowLogHolder.touch(sb.toString());
		return DefinedException.badRequest(e.getBindingResult()).parseResponse();
	}

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(DefinedException.class)
	private ResponseEntity<ServerResponseDto<Object>> definedHandler(
		DefinedException e
	) {
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.definedHandler(")
			.append(e.name())
			.append(')');
		FlowLogHolder.touch(sb.toString());
		if (!"unauthorized".equals(e.name())) {
			printError(e);
		}
		return e.parseResponse();
	}

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	private ResponseEntity<ServerResponseDto<Object>> methodNotAllowed(
		HttpRequestMethodNotSupportedException e
	) {
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.methodNotAllowed(")
			.append(SessionHelper.request().getMethod())
			.append(':')
			.append(SessionHelper.request().getRequestURI())
			.append(')');
		log.info(FlowLogHolder.touch(sb.toString()));
		return new DefinedException("method-not-allowed", e).parseResponse();
	}

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	private ResponseEntity<ServerResponseDto<Object>> notFoundHandler() {
		HttpServletRequest request = SessionHelper.request();
		String uri = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
		if (uri == null) {
			uri = request.getRequestURI();
		}
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.notFoundHandler(")
			.append(uri)
			.append(')');
		log.info(FlowLogHolder.touch(sb.toString()));
		return new DefinedException("not-found").parseResponse();
	}

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	private ResponseEntity<ServerResponseDto<Object>> notReadableHandler(
		HttpMessageNotReadableException e
	) {
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.notReadableHandler(")
			.append(e.getClass().getSimpleName())
			.append(')');
		log.info(FlowLogHolder.touch(sb.toString()));
		return new DefinedException("bad-request").parseResponse();
	}

	/**
	 * SSE timeout 에러 무시
	 * @author Artxe2
	 */
	@ExceptionHandler(AsyncRequestTimeoutException.class)
	private void asyncTimeoutHandler(
		AsyncRequestTimeoutException e
	) {}

	/**
	 * @author Artxe2
	 */
	@ExceptionHandler(Exception.class)
	private ResponseEntity<ServerResponseDto<Object>> undefinedHandler(Exception e) {
		StringBuilder sb = new StringBuilder("ExceptionResponseHandler.undefinedHandler(")
			.append(e.getClass().getSimpleName())
			.append(')');
		FlowLogHolder.touch(sb.toString());
		printError(e);
		if (SpringContext.checkProfile("local") || SpringContext.checkProfile("dev")) {
			return DefinedException.internalError(e).parseResponse();
		}
		return new DefinedException("internal-error").parseResponse();
	}

	/**
	 * @author Artxe2
	 */
	private void printError(Exception e) {
		e.setStackTrace(Arrays.copyOf(e.getStackTrace(), 5));
		Throwable t = e.getCause();
		while (t != null) {
			t.setStackTrace(Arrays.copyOf(t.getStackTrace(), 5));
			t = t.getCause();
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log.error(sw.toString());
	}
}
