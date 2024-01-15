package artxew.framework.util;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Artxe2
 */
public final class SessionHelper {
	public static final String IP = "ip";
	public static final String USER_ID = "id";
	public static final String USER_NM = "nm";
	public static final String USER_NO = "sno";
	public static final String USER_ROLES = "roles";

	/**
	 * @author Artxe2
	 */
	private SessionHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 * {@link artxew.framework.layers.auth.ctrl.AuthCtrl#signIn_User}
	 */
	public static Long sno() {
		HttpSession session = session(false);
		if (session == null) {
			return null;
		}
		return (Long) session.getAttribute(USER_NO);
	}

	/**
	 * {@link artxew.framework.layers.auth.ctrl.AuthCtrl#signIn_User}
	 *
	 * @author Artxe2
	 */
	public static String id() {
		HttpSession session = session(false);
		if (session == null) {
			return null;
		}
		return (String) session.getAttribute(USER_ID);
	}

	/**
	 * {@link artxew.framework.environment.flowlog.FlowLogInterceptor#preHandle}
	 *
	 * @author Artxe2
	 */
	public static String ip() {
		return (String) request().getAttribute(IP);
	}

	/**
	 * {@link artxew.framework.layers.auth.ctrl.AuthCtrl#signIn_User}
	 *
	 * @author Artxe2
	 */
	public static Role[] roles() {
		HttpSession session = session(false);
		if (session == null) {
			return null;
		}
		return (Role[]) session.getAttribute(USER_ROLES);
	}

	/**
	 * {@link artxew.framework.layers.auth.ctrl.AuthCtrl#signIn_User}
	 *
	 * @author Artxe2
	 */
	public static String nm() {
		HttpSession session = session(false);
		if (session == null) {
			return null;
		}
		return (String) session.getAttribute(USER_NM);
	}

	/**
	 * @author Artxe2
	 */
	public static HttpServletRequest request() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	/**
	 * @author Artxe2
	 */
	public static HttpServletResponse response() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
	}

	/**
	 * @author Artxe2
	 */
	public static HttpSession session(boolean create) {
		try {
			return request().getSession(create);
		} catch (IllegalStateException e) {
			return null;
		}
	}

	/**
	 * @author Artxe2
	 */
	public static Object get(@NonNull String key) {
		return RequestContextHolder.currentRequestAttributes()
			.getAttribute(key, 1);
	}

	/**
	 * @author Artxe2
	 */
	public static Object put(@NonNull String key, @NonNull Object value) {
		RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		Object prev = attributes.getAttribute(key, 1);
		attributes.setAttribute(key, value, 1);
		return prev;
	}

	/**
	 * @author Artxe2
	 */
	public static void remove(@NonNull String key) {
		RequestContextHolder.currentRequestAttributes()
			.removeAttribute(key, 1);
	}

	/**
	 * @author Artxe2
	 */
	public static String getCookie(String name) {
		Cookie[] cookies = request().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * @author Artxe2
	 */
	public static void setCookie(String name, String value, String path, int expiry) {
		HttpServletResponse response = response();
		if (response != null) {
			Cookie cookie = new Cookie(name, value);
			cookie.setPath(path);
			cookie.setMaxAge(expiry);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			response.addCookie(cookie);
		}
	}
}