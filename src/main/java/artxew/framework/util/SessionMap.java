package artxew.framework.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import artxew.framework.environment.authcheck.JwtHolder;
import artxew.framework.layers.oauth.dto.res.QuerySessionResDto;

public final class SessionMap {

    protected static boolean authWithJwt;

    private SessionMap() {
        throw new IllegalStateException("Utility class");
    }

    public static HttpServletRequest request() {
        return (
            (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()
        ).getRequest();
    }

    public static HttpServletResponse response() {
        return (
            (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()
        ).getResponse();
    }

    public static void signIn(QuerySessionResDto info) {
        if (authWithJwt) {
            JwtHolder.signIn(info);
        } else {
            SessionMap.put("usrId", info.getUsrId());
            SessionMap.put("auth", info.getAuth());
        }
    }

    public static void signOut() {
        if (authWithJwt) {
            // No action
        } else {
            HttpSession session = request().getSession(false);
    
            if (session != null) {
                session.setMaxInactiveInterval(0);
            }
        }
    }

    public static String usrId() {
        return (String) get("usrId");
    }

    public static String auth() {
        return (String) get("auth");
    }

    public static Object get(String key) {
        if (authWithJwt) {
            return JwtHolder.get(key);
        } else {
            return RequestContextHolder.currentRequestAttributes()
                    .getAttribute(key, 1);
        }
    }

    private static Object put(String key, Object value) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        Object prev = attributes.getAttribute(key, 1);

        attributes.setAttribute(key, value, 1);
        return prev;
    }
}