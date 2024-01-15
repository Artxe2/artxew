package artxew.framework.environment.authcheck;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Artxe2
 */
@Component
public final class AuthCheckInterceptor implements HandlerInterceptor {

	@Value("${artxew.auth-check.public-uris}")
	private String[] publicUris;

	/**
	 * @author Artxe2
	 */
	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request
		, @NonNull HttpServletResponse response
		, @NonNull Object handler
	) throws Exception {
		String concat = concat(request.getMethod(), request.getRequestURI());
		for (var s : publicUris) {
			if (concat.startsWith(s)) {
				return true;
			}
		}
		HttpSession session = request.getSession(false);
		if (session == null || SessionHelper.sno() == null) {
			String apiKey = request.getHeader("X-Api-Key");
			if (apiKey == null || !StringUtil.toHash(request.getRequestURI()).equals(apiKey)) {
				Exception e = new DefinedException("unauthorized");
				e.setStackTrace(Arrays.copyOf(e.getStackTrace(), 5));
				Throwable t = e.getCause();
				while (t != null) {
					t.setStackTrace(Arrays.copyOf(t.getStackTrace(), 5));
					t = t.getCause();
				}
				throw e;
			}
		}
		return true;
	}

	/**
	 * @author Artxe2
	 */
	private String concat(String method, String uri) {
		int mLength = method.length();
		int uLength = uri.length();
		int index = 0;
		char[] chars = new char[mLength + uLength + 1];
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
