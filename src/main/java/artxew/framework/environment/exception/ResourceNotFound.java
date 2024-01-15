package artxew.framework.environment.exception;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.flowlog.FlowLogHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@RestController
@Slf4j
public class ResourceNotFound extends BaseController implements ErrorController {

	@GetMapping("error")
	public ResponseEntity<ServerResponseDto<
	Object
	>> error(HttpServletResponse response, HttpServletRequest request) {
		String uri = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
		if (uri == null) {
			uri = request.getRequestURI();
		}
		StringBuilder sb = new StringBuilder("ResourceNotFound.notFoundHandler(")
			.append(uri)
			.append(')');
		log.info(FlowLogHolder.touch(sb.toString()));
		return new DefinedException("not-found").parseResponse();
	}
}
