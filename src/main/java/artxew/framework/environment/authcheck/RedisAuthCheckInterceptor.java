package artxew.framework.environment.authcheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public final class RedisAuthCheckInterceptor implements HandlerInterceptor {

    @Value("${artxew.auth-check.accept-for-guest-uris}")
    private String[] acceptForGuestUris;

    @Override
    public boolean preHandle(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
    ) throws Exception {
        String uri = request.getRequestURI();

        for (String s : acceptForGuestUris) {
            if (uri.startsWith(s)) {
                return true;
            }
        }

        HttpSession session = request.getSession(false);

        return session != null;
    }
}