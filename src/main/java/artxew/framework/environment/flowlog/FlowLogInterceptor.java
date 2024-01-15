package artxew.framework.environment.flowlog;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import artxew.framework.util.SessionHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@Component
@Slf4j
public final class FlowLogInterceptor implements HandlerInterceptor {

	/**
	 * @author Artxe2
	 */
	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request
		, @NonNull HttpServletResponse response
		, @NonNull Object handler
	) throws Exception {
		String uri = request.getRequestURI();
		if (!uri.equals("/error") && !uri.startsWith("/public/") && !uri.startsWith("/image/")) {
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null) {
				ip = request.getRemoteAddr();
			}
			request.setAttribute(SessionHelper.IP, ip);
			String concat = concat(request.getMethod(), uri, ip);
			// log.info("isVirtual: {}", Thread.currentThread().isVirtual());
			log.info(FlowLogHolder.push(concat));
		}
		return true;
	}

	/**
	 * @author Artxe2
	 */
	@Override
	public void afterCompletion(
		@NonNull HttpServletRequest request
		, @NonNull HttpServletResponse response
		, @NonNull Object handler
		, @Nullable Exception ex
	) throws Exception {
		String uri = request.getRequestURI();
		if (!uri.equals("/error") && !uri.startsWith("/public/") && !uri.startsWith("/image/")) {
			log.info(FlowLogHolder.pop());
		}
	}

	/**
	 * @author Artxe2
	 */
	private String concat(String method, String uri, String ip) {
		int mLength = method.length();
		int uLength = uri.length();
		int iLength = ip.length();
		int index = 0;
		char[] chars = new char[mLength + uLength + iLength + 2];
		for (int i = 0; i < iLength; i++) {
			chars[index++] = ip.charAt(i);
		}
		chars[index++] = ' ';
		for (int i = 0; i < mLength; i++) {
			chars[index++] = method.charAt(i);
		}
		chars[index++] = ':';
		for (int i = 0; i < uLength; i++) {
			chars[index++] = uri.charAt(i);
		}
		return new String(chars);
	}
}