package artxew.framework.environment.websocket;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import artxew.framework.decedent.ctrl.BaseController;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@ConditionalOnProperty("artxew.use.websocket")
@Controller
@Profile({ "dev", "local", "stg" })
@RequestMapping("websocket-ui")
@RequiredArgsConstructor
public class WebSocketUi extends BaseController {
	private byte[] html;

	/**
	 * @author Artxe2
	 */
	@GetMapping("index.html")
	public void indexHtml(HttpServletResponse response) throws IOException {
		response.reset();
		response.setContentType("text/html; charset=UTF-8");
		response.getOutputStream().write(html);
	}

	/**
	 * @author Artxe2
	 */
	@PostConstruct
	private void setHtml() throws ClassNotFoundException, IOException {
		StringBuilder sb = new StringBuilder("<!DOCTYPE html>")
			.append("<html lang=\"en\">")
			.append("	<head>")
			.append("		<meta charset=\"UTF-8\">")
			.append("		<title>WebSocket Ui</title>")
			.append("		<script src='/public/js/css-lube.min.js'></script>")
			.append("	</head>")
			.append("	<body class=\"bg=#fafafa\">")
			.append("		<div class=\"bg=#000 p=16px\">")
			.append("			<span class=\"c=#fff bold fs=32px ff=sans-serif\">WebSocket Ui</span>")
			.append("		</div>")
			.append("		<div endpoints class=\"xw=1460px m=0_auto p=50px_20px_0_20px\">")
			.append("		</div>")
			.append("		<script src='/public/js/websocket-ui.js'></script>")
			.append("		<script>").append(getMetaJson()).append("</script>")
			.append("	</body>")
			.append("</html>");
		html = sb.toString()
			.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * @author Artxe2
	 */
	private StringBuilder getMetaJson() throws ClassNotFoundException, IOException {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(ServerEndpoint.class));
		StringBuilder sb = new StringBuilder("const json = [");
		for (BeanDefinition bean : scanner.findCandidateComponents("artxew")) {
			Class<?> clazz = Class.forName(bean.getBeanClassName());
			Annotation serverEndpointAnnotation = clazz.getAnnotation(ServerEndpoint.class);
			Annotation wsApiAnnotation = clazz.getAnnotation(WsApi.class);
			if (serverEndpointAnnotation != null) {
				String uri = (String) AnnotationUtils.getValue(serverEndpointAnnotation);
				String tag = wsApiAnnotation != null
					? (String) AnnotationUtils.getValue(wsApiAnnotation)
					: clazz.getSimpleName();
				sb.append("{uri:\"").append(uri).append("\",tag:\"").append(tag).append("\"},");
			}
		}
		return sb.append("]");
	}
}