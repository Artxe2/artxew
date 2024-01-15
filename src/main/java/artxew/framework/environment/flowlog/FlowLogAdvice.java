package artxew.framework.environment.flowlog;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@Aspect
@Component
@Slf4j
public final class FlowLogAdvice {

	@Value("${artxew.domain}")
	private String domain;

	/**
	 * @author Artxe2
	 */
	@Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(artxew.framework.environment.flowlog.CurlLog)")
	public void curlLog(JoinPoint joinPoint) {
		HttpServletRequest request = SessionHelper.request();
		String method = request.getMethod();
		String queryString = request.getQueryString();
		Enumeration<String> headers = request.getHeaderNames();
		StringBuilder sb = new StringBuilder("curl -X '")
			.append(method)
			.append("' \\\n\t'")
			.append(domain)
			.append(request.getRequestURI());
		if (queryString != null) {
			sb.append('?')
				.append(queryString);
		}
		sb.append('\'');
		String name;
		while(headers.hasMoreElements()){
			name = headers.nextElement();
			sb.append(" \\\n\t-H '")
				.append(name)
				.append(": ")
				.append(request.getHeader(name))
				.append('\'');
		}
		if (!"GET".equals(method)) {
			Annotation[][] annotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
			int length = annotations.length;
			for (int i = 0; i < length; i++) {
				for (Annotation a : annotations[i]) {
					if (a instanceof RequestBody) {
						sb.append(" \\\n\t-d '")
							.append(StringUtil.toPrettyJson(joinPoint.getArgs()[i]))
							.append('\'');
						break;
					}
				}
			}
		}
		log.info(sb.toString());
	}

	/**
	 * @author Artxe2
	 */
	@Before("within(artxew.*.layers.*.svc.*Svc)")
	public void before(JoinPoint joinPoint) {
		log.info(FlowLogHolder.push(joinPoint.getSignature().toShortString()));
	}

	/**
	 * @author Artxe2
	 */
	@After("within(artxew.*.layers.*.svc.*Svc)")
	public void after(JoinPoint joinPoint) {
		FlowLogHolder.pop();
	}
}